package com.dsvl.flood.service.impl;

import com.dsvl.flood.UdpHelper;
import com.dsvl.flood.UdpMsgBuilder;
import com.dsvl.flood.service.UnregisterService;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class UnregisterServiceImpl implements UnregisterService {
    @Override
    public Boolean unregister(InetAddress bootstrapAddress, int bootstrapServerPort,
                              InetAddress nodeAddress, int nodeUdpPort, String username) {
        //send and receive - to stay synced with the bootstrap server
        String unregMsg = UdpMsgBuilder.buildUnregisterMsg(nodeAddress.getHostAddress(), nodeUdpPort, username);
        DatagramPacket responsePacket =  UdpHelper.sendAndReceiveMessage(unregMsg, bootstrapAddress, bootstrapServerPort,
                nodeUdpPort, 2000);

        //process response
        if (responsePacket.getAddress() == null) { // no response from bootstrap server
            return false;
        }
        String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
        return "0012 UNROK 0".equals(response);
    }
}
