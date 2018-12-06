package com.dsvl.flood.service;

import com.dsvl.flood.Neighbour;

import java.net.InetAddress;
import java.util.List;

public interface LeaveService {
    /**
     * msg: {@code length LEAVE IP_address port_no 2 IP_address port_no IP_address port_no}
     * @param neighbourAddress neighbour to inform
     * @param neighbourPort    neighbour's port
     * @param nodeAddress my ip
     * @param nodePort my udpPort
     * @param tempUdpPort
     * @param myNeighbours my neighbours
     */
    Boolean leave(InetAddress neighbourAddress, Integer neighbourPort,
                  InetAddress nodeAddress, Integer nodePort, int tempUdpPort, List<Neighbour> myNeighbours);
}
