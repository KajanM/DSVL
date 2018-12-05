package com.dsvl.flood;

import com.dsvl.flood.exceptions.ErroneousResponseException;
import com.dsvl.flood.model.Log;
import com.dsvl.flood.service.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static com.dsvl.flood.Constants.Status.REGISTERED;

/**
 * This {@code Component} starts with the application and attempts
 * to register with the bootstrap server.
 * <br>
 * If register attempt is failed for some reason
 * this will reattempt to register after waiting for five seconds.
 * <br>
 * Once registration is successful, it then starts listening to incoming
 * UDP messages forever.
 * This {@code CommandLineRunner} exists with the main application
 */
@Component
public class UdpServer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);

    @Autowired
    private Node node;

    @Autowired
    private LogRepository logRepository;

    @Override
    public void run(String... args) {

        while (!node.isRegistered()) {
            logger.info("Attempting to register with the bootstrap server");

            if (node.register()) {
                node.setRegistered(true);
                node.setStatus(REGISTERED);
                break;
            }

            // TODO: instead of sleeping for fixed 5 seconds apply some incremental logic
            logger.info("Sleeping for 5 seconds");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //ignore
            }
        }

        //try to join the network 10 times and give up
        List<Neighbour> existingNodes = node.getExistingNodes();
        for (int i = 10; i > -1; i--) {
            logger.info("Attempting to connect to the network: trial {}", i);
            if (node.joinNetwork(existingNodes)) break;

            // TODO: instead of sleeping for fixed 5 seconds apply some incremental logic
            logger.info("Sleeping for 5 seconds");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //ignore
            }
            if (i == 0) {
                logger.warn("Unable to connect to the network");
            }
        }

        // Create an ever running UDP receiving socket
        try (DatagramSocket socket = new DatagramSocket(node.getNodeUdpPort())) {
            logger.debug("UDP server started for incoming messages at port {}", node.getNodeUdpPort());
            byte[] buffer;

            new Thread(() -> { // ping is done within a seperete thread
                node.sendPingMessage();
            }).start();

            while (true) {
                if (node.isLeaving) {
                    logger.debug("Stopping ever running UDP server port at {}", node.getNodeUdpPort());
                    break;
                }
                buffer = new byte[65536];
                DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(incomingPacket);
                if (incomingPacket.getData().length != 0) {
                    String receivedData = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
                    Log log = new Log(
                            incomingPacket.getAddress().getHostAddress() + ":" + incomingPacket.getPort(),
                            "this",
                            "UDP",
                            receivedData
                    );
                    logRepository.save(log);
                    logger.info("Received UDP message from {}:{} {}", incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort(), receivedData);
                    MessageDecoder messageDecoder = MessageDecoder.getInstance();
                    try {
                        MessageObject msgObject = messageDecoder.decode(incomingPacket.getData(), incomingPacket.getLength());
                        msgObject.setSenderIP(String.valueOf(incomingPacket.getAddress()));
                        msgObject.setSenderPort(incomingPacket.getPort());
                        respond(msgObject, incomingPacket.getAddress(), incomingPacket.getPort());
                    } catch (ErroneousResponseException e) {
                        logger.info("Erroneous response received: {}", e.getMessage());
                    }
                }
            }
        } catch (SocketException e) {
            logger.error("Unable to open UDP socket for receiving", e);
        } catch (IOException e) {
            logger.error("Unable to receive UDP message", e);
        }

    }

    private void respond(MessageObject msgObject, InetAddress senderIP, int senderPort) {
        /*
         * Messages That Will Not Be Handled Here
         *
         * REGOK - handled at RegisterServiceImpl because we have nothing else to do until we until we reg with bootstrap server
         * JOINOK - handled at JoinServiceImpl because we have nothing else to do until we join the network
         *
         * */
        switch (msgObject.getMsgType()) {
            // TODO: UNREG, UNROK
            case "JOIN":
                Neighbour newNeighbour = msgObject.getJoinRequester();
                if (newNeighbour != null) {
                    newNeighbour.settTL(5);
                    List<Neighbour> neighbours = node.getNeighbours();
                    for(Neighbour neighbour: neighbours){
                        if(neighbour.getAddress() == newNeighbour.getAddress() && neighbour.getPort() == newNeighbour.getPort()){
                            UdpHelper.sendMessage("0016 JOINOK 9999", senderIP, senderPort);
                            return;
                        }
                    }
                    node.getNeighbours().add(newNeighbour);
                    logger.info("New node added as neighbor, IP address: {}, port: {}",
                            newNeighbour.getAddress(), newNeighbour.getPort());
                    UdpHelper.sendMessage("0013 JOINOK 0", senderIP, senderPort);
                } else {
                    UdpHelper.sendMessage("0016 JOINOK 9999", senderIP, senderPort);
                }
                break;
            case "SER":
                logger.info("Search query has found, file name: {}, hops {}, IP address: {}, port: {}",
                        msgObject.getFile_name(), msgObject.getHops(),msgObject.getSearch_ip(),msgObject.getSearch_udp_Port());
                new Thread(() -> { // search is done within a seperete thread
                try {

                    List<File> search_results = node.search(msgObject);
                    String file_name_string = "";
                    String query = "SEROK" + " " + String.valueOf(search_results.size()) + " " + String.valueOf(node.getNodeAddress()) + " " + String.valueOf(node.getTcpPort()) + " " + String.valueOf(msgObject.getHops()) + " ";
                    for (int i = 0; i < search_results.size(); i++) {
                        String fn = search_results.get(i).getFileName();
                        fn = fn.replaceAll(" ", "_");
                        file_name_string += fn + " ";
                    }
                    query += file_name_string;
                    String length = String.format("%04d", query.length() + 4);
                    query = length + " " + query;
//                    System.out.println(query);
//                    System.out.println(senderIP);
                    InetAddress inetAddress = InetAddress.getByName(msgObject.getSearch_ip());
                    UdpHelper.sendMessage(query,inetAddress, msgObject.getSearch_udp_Port());
                }catch(Exception e){

                    String query="SEROK"+" "+"9998 "+ String.valueOf(node.getNodeAddress()) + " " + String.valueOf(node.getTcpPort()) + " " + String.valueOf(msgObject.getHops());
                    String length = String.format("%04d", query.length() + 4);
                    query = length + " " + query;

                    InetAddress inetAddress = null;
                    try {
                        inetAddress = InetAddress.getByName(msgObject.getSearch_ip());
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    }
                    UdpHelper.sendMessage(query, inetAddress, msgObject.getSearch_udp_Port());
                }
        }).start();

            case "SEROK":



                if (msgObject.getNo_of_results()==9999){
                    logger.info("Search response has recieved:  failure due to node unreachable");
                }
                else if (msgObject.getNo_of_results()==9998){
                logger.info("Search response has recieved:  some other error");
                }
                else {
                    logger.info("Search response has recieved  Number of results: {}, hops {}, IP address: {}, TCPport: {}",
                            msgObject.getNo_of_results(), msgObject.getHops(),msgObject.getSearch_result_ip(),msgObject.getSearch_result_tcp_Port());
                    // creating the tcp connection and file transfering
                }

            case "PNG":
                new Thread(() -> { // ping is done within a seperete thread
                    logger.info("PNG message recieved: SenderIP: {}, Port: {}",
                            msgObject.getPingIP(), msgObject.getPingPort());
                List<Neighbour> routingTable=node.getNeighbours();
                List<Neighbour> returnRoutingTable=new ArrayList<>();

                for (Neighbour n: routingTable ) {
                    if (!n.getAddress().toString().equals(msgObject.getSenderIP().toString())) { //overrride neighbours equals method
                        returnRoutingTable.add(n); // add all except message querried node
                    }
                }

                String routingString="";
                for (Neighbour x:returnRoutingTable ) { // creating the query
                    routingString+=x.getAddress().getHostName()+":"+String.valueOf(x.getPort())+"_";
                }
                String command="PINGOK";
                String noOfResults=String.valueOf(returnRoutingTable.size());
                String ip=node.getNodeAddress();
                String port= String.valueOf(node.getNodeUdpPort());

                String query= command+" "+ noOfResults+" "+ip+" "+port+" "+routingString;
                String length = String.format("%04d", query.length() + 4);
                query = length + " " + query;
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getByName(msgObject.getPingIP());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                UdpHelper.sendMessage(query, inetAddress, msgObject.getPingPort());
                logger.info("PNGOK message sent to: SenderIP: {}, Port: {}",
                            msgObject.getPingIP(), msgObject.getPingPort());
                }).start();

            case "PNGOK":
                new Thread(() -> { // pingok is processed within a separate thread
                    logger.info("PNGOK message recieved: SenderIP{}, Port {}",
                            msgObject.getPingOkIP(), msgObject.getPingOkPort());
                        for (Neighbour j:node.getNeighbours()) {
                            if(j.getAddress().getHostName().equals(msgObject.getPingOkIP())){
                                j.settTL(j.gettTL()+1);
                            }
                        }
                        List<Neighbour> newNeighbours=new ArrayList<>();
                        for (Neighbour i:msgObject.getRoutingList()) {
                            int count=0;
                            for (Neighbour j:node.getNeighbours()) {
                                if(!i.getAddress().getHostName().equals(j.getAddress().getHostName())){
                                    count+=1;
                                }
                            }
                            if(count==node.getNeighbours().size()){
                                newNeighbours.add(i);
                                // todo potential neighbours list
                            }

                        }
//                    newNeighbours; TODO subhashini this is the unique neighbours list you can call your function here and input this list as a paremeter

                }).start();
                break;
            case "LEAVE":
                Neighbour leavingNeighbour = msgObject.getLeavingNode();
                if (leavingNeighbour != null) {
                    for (Neighbour neighbour : node.getNeighbours()) {
                        if (leavingNeighbour.getAddress().equals(neighbour.getAddress()) &&
                                leavingNeighbour.getPort() == neighbour.getPort()) {
                            node.getNeighbours().remove(neighbour);
                            logger.info("Neighbour {}:{} gracefully left the network",
                                    neighbour.getAddress().getHostName(), neighbour.getPort());
                            UdpHelper.sendMessage("0014 LEAVEOK 0", senderIP, senderPort);
                            List<Neighbour> leaversNeighbours = msgObject.getLeaversNeighbors();
                            if (leaversNeighbours != null && leaversNeighbours.isEmpty() && node.getNeighbours().size()<4) {
                                logger.info("Trying to add neighbours sent by the node just left");
                                node.join(leaversNeighbours);
                            }
                            return;
                        }
                    }
                }
                UdpHelper.sendMessage("0016 LEAVEOK 9999", senderIP, senderPort);
                break;
            case "NONE":
                //ignore
                break;
            default:
                UdpHelper.sendMessage("0010 ERROR", senderIP, senderPort);
                break;
        }
    }
}
