package com.dsvl.flood;

import com.dsvl.flood.exceptions.ErroneousResponseException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class MessageDecoder {
    public static final String REGOK = "REGOK";
    public static final String UNROK = "UNROK";
    public static final String JOIN = "JOIN";
    public static final String LEAVE = "LEAVE";
    public static final String SER = "SER";
    public static final String SEROK = "SEROK";
    public static final String ERROR = "ERROR";
    public static final String PNG = "PNG";
    public static final String PNGOK = "PNGOK";

    public static MessageObject decode(byte[] data, int dataLength) throws ErroneousResponseException {
        String s = new String(data, 0, dataLength);

        StringTokenizer st = new StringTokenizer(s, " ");

        MessageObject messageObject = new MessageObject();
        if (st.countTokens() < 2) {
            messageObject.setMsgType("NONE");
            return messageObject;
        }
        String length = st.nextToken();
        String command = st.nextToken();

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
                    Node.latestSearchResults = new ArrayList<>();
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
                    int hops = Integer.parseInt(st.nextToken()) + 1;
                    while (st.hasMoreTokens()) {
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

                    if (!st.hasMoreElements()) {
                        return messageObject;
                    }
                    //expected ---> length LEAVE IP_address port_no 2 IP_address port_no IP_address port_no
                    String numberOfAddressesSt = st.nextToken();
                    if ("1".equals(numberOfAddressesSt) || "2".equals(numberOfAddressesSt)) {
                        int numberOfNodes = Integer.parseInt(numberOfAddressesSt);
                        List<Neighbour> addresses = new ArrayList<>();
                        for (int i = 0; i < numberOfNodes; i++) {
                            try {
                                addresses.add(new Neighbour(InetAddress.getByName(st.nextToken()), Integer.parseInt(st.nextToken())));
                            } catch (UnknownHostException e) {
                                continue;
                            }
                        }
                        messageObject.setLeaversNeighbors(addresses);
                    }
                } catch (UnknownHostException e) {
                    //ignore
                }
                break;

            case PNG:
                messageObject.setMsgType(PNG);
                try {
                    String ip = st.nextToken();
                    int udp_port = Integer.parseInt(st.nextToken());

                    messageObject.setPingIP(ip);
                    messageObject.setPingPort(udp_port);
                } catch (Exception e) {

                }
                break;

            case PNGOK:
                messageObject.setMsgType(PNGOK);
                try {
                    String noOfResults = st.nextToken();
                    String ip = st.nextToken();
                    int udp_port = Integer.parseInt(st.nextToken());
                    String[] listOfNeighbours = st.nextToken().split("_");

                    List<Neighbour> neighboutList = new ArrayList<>();
                    for (String str : listOfNeighbours) {
                        String[] str2 = str.split(":");
                        Neighbour n1 = new Neighbour(InetAddress.getByName(str2[0]), Integer.parseInt(str2[1]));
                        neighboutList.add(n1);
                    }
                    messageObject.setRoutingList(neighboutList);
                    messageObject.setPingOkIP(ip);
                    messageObject.setPingOkPort(udp_port);
                    messageObject.setNo_of_results(Integer.parseInt(noOfResults));

                } catch (Exception e) {

                }
                break;
        }
        return messageObject;
    }
}
