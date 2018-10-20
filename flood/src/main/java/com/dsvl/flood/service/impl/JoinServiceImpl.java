package com.dsvl.flood.service.impl;

import com.dsvl.flood.UdpHelper;
import com.dsvl.flood.UdpMsgBuilder;
import com.dsvl.flood.service.JoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class JoinServiceImpl implements JoinService {

    private static final Logger logger = LoggerFactory.getLogger(JoinServiceImpl.class);

    @Override
    public Boolean join(InetAddress peerAddress, Integer peerPort, InetAddress nodeAddress, Integer nodePort) {
        String joinMsg = UdpMsgBuilder.buildJoinMsg(nodeAddress.getHostAddress(), nodePort);

        if (!UdpHelper.sendMessage(joinMsg, peerAddress, peerPort, nodePort)) {
            logger.warn("Joining the network via peer {} failed", peerAddress);
            return false;
        }
        if (UdpHelper.receiveMessage(nodePort, 2000) != null) {
            logger.info("Successfully joined the network via peer {}", peerAddress);
            return true;
        }
        return false;
    }
}
