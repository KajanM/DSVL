package com.dsvl.flood.service;

import java.net.InetAddress;

public interface JoinService {

    /**
     * msg: {@code length JOIN IP_address port_no}
     * @param peerAddress
     * @param peerPort
     * @param nodeAddress
     * @param nodePort
     */
    Boolean join(InetAddress peerAddress, Integer peerPort, InetAddress nodeAddress, Integer nodePort);

}
