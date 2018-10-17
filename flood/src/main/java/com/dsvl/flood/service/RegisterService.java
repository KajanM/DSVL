package com.dsvl.flood.service;

import java.net.InetAddress;

public interface RegisterService {

    /**
     * msg: {@code length REG IP_address port_no username}
     *
     * @param bootstrapAddress
     * @param nodeAddress
     * @param bootstrapServerPort
     * @param nodePort
     * @param username
     * @return  whether the registration is successful or not
     */
    Boolean register(InetAddress bootstrapAddress, InetAddress nodeAddress, Integer bootstrapServerPort, Integer nodePort, String username);

}
