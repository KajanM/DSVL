package com.dsvl.flood;

import com.dsvl.flood.exceptions.RequestFailedException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
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

    public MessageObject decode(byte[] data, int dataLength) throws RequestFailedException{
        String s = new String(data, 0, dataLength);
        StringTokenizer st = new StringTokenizer(s, " ");

        String length = st.nextToken();
        String command = st.nextToken();

        MessageObject messageObject = new MessageObject();
        switch (command) {
            case REGOK:
                //expected response ---> length REGOK no_nodes IP_1 port_1 IP_2 port_2
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
                            reason = "REG unsuccessful -Already registered, unregister first";
                        case "9997":
                            reason = "REG unsuccessful -Registered to another user, try a different IP and port";
                        case "9996":
                            reason = "REG unsuccessful - Can’t register. Bootstrap server full.";
                        default:
                            reason = "REG unsuccessful - Error registering to Bootstrap server";
                    }
                    throw new RequestFailedException(reason);
                }
        }
        return messageObject;
    }
}
