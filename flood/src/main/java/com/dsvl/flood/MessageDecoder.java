package com.dsvl.flood;

import com.dsvl.flood.exceptions.ErroneousResponseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class MessageDecoder {
    public static final String REGOK = "REGOK";
    public static final String UNROK = "UNROK";
    public static final String JOIN = "JOIN";
    public static final String JOINOK = "JOINOK";
    public static final String LEAVE = "LEAVE";
    public static final String LEAVEOK = "LEAVEOK";
    public static final String SER = "SER";
    public static final String SEROK = "SEROK";
    public static final String ERROR = "ERROR";

    private static volatile MessageDecoder instance = null;

    private MessageDecoder() {}

    public static MessageDecoder getInstance() {
        if (instance == null) {
            synchronized(MessageDecoder.class) {
                if (instance == null) {
                    instance = new MessageDecoder();
                }
            }
        }
        return instance;
    }

//    public MessageObject

    public MessageObject decode(byte[] data, int dataLength) throws ErroneousResponseException {
        String s = new String(data, 0, dataLength);

        StringTokenizer st = new StringTokenizer(s, " ");

        String length = st.nextToken();
        String command = st.nextToken();

        MessageObject messageObject = new MessageObject();
        switch (command) {
            case REGOK:
                //expected response ---> length REGOK no_nodes IP_1 port_1 IP_2 port_2
                messageObject.setMsgType(REGOK);
                String numberOfNodesSt = st.nextToken();
                if (numberOfNodesSt.length() == 1) { // REGOK with success
                    int numberOfNodes = Integer.parseInt(numberOfNodesSt);
                    List<Neighbour> addresses = new ArrayList<>();
                    for (int i = 0; i < numberOfNodes; i++) {
                        try {
                            addresses.add(new Neighbour(InetAddress.getByName(st.nextToken()), Integer.parseInt(st.nextToken())));
                        } catch (UnknownHostException e) {
                            continue;
                        }
                    }
                    messageObject.setExistingNodes(addresses);
                } else { // REGOK with error
                    String reason;
                    switch (numberOfNodesSt) {
                        case "9999":
                            reason = "REG unsuccessful - Error in the command";
                        case "9998":
                            reason = "REG unsuccessful - Already registered, unregister first";
                        case "9997":
                            reason = "REG unsuccessful - Registered to another user, try a different IP and port";
                        case "9996":
                            reason = "REG unsuccessful - Can’t register. Bootstrap server full.";
                        default:
                            reason = "REG unsuccessful - Error registering to Bootstrap server";
                    }
                    throw new ErroneousResponseException(reason);
                }
                break;
            case JOIN:
                //expected ---> length JOIN IP_address port_no
                messageObject.setMsgType(JOIN);
                try {
                    String ip = st.nextToken();
                    int port = Integer.parseInt(st.nextToken());
                    messageObject.setJoinRequester(new Neighbour(InetAddress.getByName(ip), port));
                } catch (UnknownHostException e) {
                    //ignore
                }
                break;
            case SER:
                messageObject.setMsgType(SER);

                try {
                    Node.latestSearchResults=new ArrayList<>();
                    String ip = st.nextToken();
                    int port = Integer.parseInt(st.nextToken());
                    messageObject.setSearch_udp_Port(port);
                    messageObject.setSearch_ip(ip);
                    String file_name = st.nextToken();
                    int hops = Integer.parseInt(st.nextToken());
                    messageObject.setFile_name(file_name);
                    messageObject.setHops(hops);
                } catch (Exception e) {
                    //ignore
                }
                break;
            case SEROK:
                messageObject.setMsgType(SEROK);
                try {
                    int no_of_results = Integer.parseInt(st.nextToken());
                    String ip = st.nextToken();
                    int tcp_port = Integer.parseInt(st.nextToken());
                    int hops = Integer.parseInt(st.nextToken())+1;
                    while(st.hasMoreTokens()){
                        Node.latestSearchResults.add(st.nextToken());
                    }
                    messageObject.setNo_of_results(no_of_results);
                    messageObject.setSearch_result_ip(ip);
                    messageObject.setSearch_result_tcp_Port(tcp_port);
                    messageObject.setHops(hops);
                } catch (Exception e) {
                    //ignore
                }
                break;
            case LEAVE:
                //expected ---> length LEAVE IP_address port_no
                messageObject.setMsgType(LEAVE);
                try {
                    String ip = st.nextToken();
                    int port = Integer.parseInt(st.nextToken());
                    messageObject.setLeavingNode(new Neighbour(InetAddress.getByName(ip), port));
                } catch (UnknownHostException e) {
                    //ignore
                }
                break;
        }
        return messageObject;
    }
}
