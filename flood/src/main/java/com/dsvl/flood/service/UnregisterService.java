package com.dsvl.flood.service;

import java.net.InetAddress;

public interface UnregisterService {

    /**
     * msg: {@code length UNREG IP_address port_no username}
     *
     * @param bootstrapAddress     - bootstrap server address
     * @param bootstrapServerPort  - bootstrap server port
     * @param nodeAddress          - this node address
     * @param nodeUdpPort          - this node's udp port
     * @param username             - username of this p2p network
     * @return  whether the registration is successful or not
     */
    Boolean unregister(InetAddress bootstrapAddress, int bootstrapServerPort,
                     InetAddress nodeAddress, int nodeUdpPort, String username);
}
