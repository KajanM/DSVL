package com.dsvl.flood.service.impl;

import com.dsvl.flood.*;
import com.dsvl.flood.service.LeaveService;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Override
    public Boolean leave(InetAddress neighbourAddress, Integer neighbourPort,
                         InetAddress nodeAddress, Integer nodeUdpPort, Integer tempUdpPort, List<Neighbour> myNeighbours) {
        String leaveMsg = UdpMsgBuilder.buildLeaveMsg(nodeAddress.getHostAddress(), nodeUdpPort, myNeighbours);
        DatagramPacket responsePacket = UdpHelper.sendAndReceiveMessage(leaveMsg, neighbourAddress, neighbourPort,
                tempUdpPort, 5000);
        if (responsePacket.getAddress() == null) { // no response from peer
            return false;
        }
        String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
        return "0014 LEAVEOK 0".equals(response);
    }
}
