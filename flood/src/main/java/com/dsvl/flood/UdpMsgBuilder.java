package com.dsvl.flood;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

public class UdpMsgBuilder {

    /**
     * length SER IP port file_name hops
     */
    private static final String SEARCH_MSG_TEMPLATE = "{0} SER {1} {2,number,#} {3} {4}";

    /**
     * length REG IP_address port_no username
     */
    private static final String REGISTER_MSG_TEMPLATE = "{0} REG {1} {2,number,#} {3}";

    /**
     * length JOIN IP_address port_no
     */
    private static final String JOIN_MSG_TEMPLATE = "{0} JOIN {1} {2,number,#}";

    /**
     * length LEAVE IP_address port_no
     * or
     * length LEAVE IP_address port_no 2 IP_address port_no IP_address port_no
     */
    private static final String LEAVE_MSG_TEMPLATE = "{0} LEAVE {1} {2,number,#}{3}";

    /**
     * Returns a {@code String} of the format {@code length SER IP port file_name hops}
     *
     * @param nodeAddress
     * @param nodeUdpPort
     * @param fileName
     * @param hops
     * @return the search message
     */
    public static String buildSearchMsg(String nodeAddress, Integer nodeUdpPort, String fileName, Integer hops) {
//        length SER IP port file_name hops
        /*
         * 4 - length
         * 5 - spaces
         * 3 - SER
         * __
         * 12
         * */
        int length = 12 + nodeAddress.length() + nodeUdpPort.toString().length() + fileName.length() + hops.toString().length();

        return MessageFormat.format(SEARCH_MSG_TEMPLATE, String.format("%04d", length), nodeAddress, nodeUdpPort, fileName, hops);
    }

    /**
     * Returns a {@code String} of the format {@code length REG IP_address port_no username}
     * @param nodeAddress
     * @param nodeUdpPort
     * @param username
     * @return the register message
     */
    public static String buildRegisterMsg(String nodeAddress, Integer nodeUdpPort, String username) {
        /*
         * 4 - length
         * 4 - spaces
         * 3 - REG
         * __
         * 11
         * */
        int length = 11 + nodeAddress.length() + nodeUdpPort.toString().length() + username.length();

        return MessageFormat.format(REGISTER_MSG_TEMPLATE, String.format("%04d", length), nodeAddress, nodeUdpPort, username);
    }

    /**
     * Returns a {@code String} of the format {@code length JOIN IP_address port_no}
     * @param nodeAddress my ip
     * @param nodeUdpPort my listening udp port
     * @return the join message
     */
    public static String buildJoinMsg(String nodeAddress, Integer nodeUdpPort) {
        /*
         * 4 - length
         * 3 - spaces
         * 4 - JOIN
         * __
         * 11
         * */
        int length = 11 + nodeAddress.length() + nodeUdpPort.toString().length();

        return MessageFormat.format(JOIN_MSG_TEMPLATE, String.format("%04d", length), nodeAddress, nodeUdpPort);
    }

    /**
     * Returns a {@code String} of the format {@code length LEAVE IP_address port_no 2 IP_address port_no IP_address port_no}
     * @param nodeAddress my ip
     * @param nodeUdpPort my listening udp port
     * @return the leave message
     */
    public static String buildLeaveMsg(String nodeAddress, Integer nodeUdpPort, List<Neighbour> myNeighbours) {
        /*
         * 4 - length
         * 3 - spaces
         * 5 - LEAVE
         * __
         * 12
         * */
        int length = 12 + nodeAddress.length() + nodeUdpPort.toString().length();
        String messageLatterPart = "";
        if(myNeighbours.isEmpty()){
            //do nothing
        }
        else if (myNeighbours.size() == 1) {
            messageLatterPart += " 1 " + myNeighbours.get(0).getIpAddress().getHostName() + " " + myNeighbours.get(0).getUdpPort();
        } else if (myNeighbours.size() == 2) {
            messageLatterPart += " 2 " + myNeighbours.get(0).getIpAddress().getHostName() + " " + myNeighbours.get(0).getUdpPort()
                    + " " + myNeighbours.get(1).getIpAddress().getHostName() + " " + myNeighbours.get(1).getUdpPort();
        } else {
            Random r = new Random();
            int Low = 0;
            int High = myNeighbours.size();
            int random_1 = r.nextInt(High-Low) + Low;
            int random_2 = r.nextInt(High-Low) + Low;
            while (random_1 == random_2) {
                random_2 = r.nextInt(High-Low) + Low;
            }
            messageLatterPart += " 2 " + myNeighbours.get(random_1).getIpAddress().getHostName() + " " + myNeighbours.get(random_1).getUdpPort()
                    + " " + myNeighbours.get(random_2).getIpAddress().getHostName() + " " + myNeighbours.get(random_2).getUdpPort();
        }
        length += messageLatterPart.length();
        return MessageFormat.format(LEAVE_MSG_TEMPLATE, String.format("%04d", length), nodeAddress, nodeUdpPort, messageLatterPart);
    }
}
