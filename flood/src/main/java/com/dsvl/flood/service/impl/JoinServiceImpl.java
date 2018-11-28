package com.dsvl.flood.service.impl;

import com.dsvl.flood.*;
import com.dsvl.flood.service.JoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;

@Service
public class JoinServiceImpl implements JoinService {

    private static final Logger logger = LoggerFactory.getLogger(JoinServiceImpl.class);

    @Override
    public Boolean join(InetAddress peerAddress, Integer peerPort, InetAddress nodeAddress, Integer nodeUdpPort) {
        String joinMsg = UdpMsgBuilder.buildJoinMsg(nodeAddress.getHostAddress(), nodeUdpPort);
        DatagramPacket responsePacket =  UdpHelper.sendAndReceiveMessage(joinMsg, peerAddress, peerPort,
                nodeUdpPort, 2000);

        if (responsePacket.getAddress() == null) { // no response from peer
            return false;
        }
        String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
        return "0013 JOINOK 0".equals(response);
    }
}
