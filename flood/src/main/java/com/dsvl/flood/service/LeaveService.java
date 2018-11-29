package com.dsvl.flood.service;

import com.dsvl.flood.Neighbour;

import java.net.InetAddress;
import java.util.List;

public interface LeaveService {
    /**
     * msg: {@code length JOIN IP_address port_no}
     * @param neighbourAddress neighbour to inform
     * @param neighbourPort    neighbour's port
     * @param nodeAddress my ip
     * @param nodePort my udpPort
     */
    Boolean leave(InetAddress neighbourAddress, Integer neighbourPort, InetAddress nodeAddress, Integer nodePort);
}
