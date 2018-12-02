package com.dsvl.flood;

import java.text.MessageFormat;

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
     * length JOINOK value
     */
    private static final String JOINOK_MSG_TEMPLATE = "{0} JOINOK {1,number,#}";

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
     * Returns a {@code String} of the format {@code length JOINOK value}
     * @param value 0 if success, else 9999
     * @return the join ok message
     */
    public static String buildJoinOkMsg(Integer value) {
        /*
         * 4 - length
         * 2 - spaces
         * 6 - JOINOK
         * __
         * 12
         * */
        int length = 12 + value.toString().length();

        return MessageFormat.format(JOINOK_MSG_TEMPLATE, String.format("%04d", length), value);
    }

}
