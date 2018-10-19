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
    public Boolean register(InetAddress bootstrapAddress, InetAddress nodeAddress, Integer bootstrapServerPort, Integer nodePort, String username) {

        String regMsg = UdpMsgBuilder.buildRegisterMsg(nodeAddress.getHostAddress(), nodePort, username);

        if (!UdpHelper.sendMessage(regMsg, bootstrapAddress, bootstrapServerPort, nodePort)) {
            logger.warn("Registering with the bootstrap server failed");
            return false;
        }

        if (UdpHelper.receiveMessage(nodePort, 2000) != null) {
            logger.info("Successfully registered with the bootstrap server");
            return true;
        }

        return false;
    }

}
