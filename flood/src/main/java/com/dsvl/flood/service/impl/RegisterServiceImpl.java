package com.dsvl.flood.service.impl;

import com.dsvl.flood.UdpHelper;
import com.dsvl.flood.UdpMsgBuilder;
import com.dsvl.flood.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class RegisterServiceImpl implements RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Override
    public Boolean register(InetAddress bootstrapAddress, InetAddress nodeAddress, Integer bootstrapServerPort, Integer nodeUdpPort, String username) {

        String regMsg = UdpMsgBuilder.buildRegisterMsg(nodeAddress.getHostAddress(), nodeUdpPort, username);

        if (!UdpHelper.sendMessage(regMsg, bootstrapAddress, bootstrapServerPort, nodeUdpPort)) {
            logger.warn("Registering with the bootstrap server failed");
            return false;
        }

        if (UdpHelper.receiveMessage(nodeUdpPort, 2000) != null) {
            // TODO: Subhashini, REGOK specific implementation should go here
            logger.info("Successfully registered with the bootstrap server");
            return true;
        }

        return false;
    }

}
