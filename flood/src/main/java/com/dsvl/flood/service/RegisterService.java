package com.dsvl.flood.service;

import com.dsvl.flood.Neighbour;

import java.net.InetAddress;
import java.util.List;

public interface RegisterService {

    /**
     * msg: {@code length REG IP_address port_no username}
     *
     * @param bootstrapAddress     - bootstrap server address
     * @param bootstrapServerPort  - bootstrap server port
     * @param nodeAddress          - this node address
     * @param nodeUdpPort          - this node's udp port
     * @param username             - username of this p2p network
     * @param existingNodes        - an ArrayList to add the ip addresses sent by bootstrap server
     * @return  whether the registration is successful or not
     */
    Boolean register(InetAddress bootstrapAddress, int bootstrapServerPort,
                     InetAddress nodeAddress, int nodeUdpPort, String username, List<Neighbour> existingNodes);
}
