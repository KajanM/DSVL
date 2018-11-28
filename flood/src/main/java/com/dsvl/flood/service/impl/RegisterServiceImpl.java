package com.dsvl.flood.service.impl;

import com.dsvl.flood.*;
import com.dsvl.flood.exceptions.ErroneousResponseException;
import com.dsvl.flood.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Override
    public Boolean register(InetAddress bootstrapAddress, int bootstrapServerPort,
                            InetAddress nodeAddress, int nodeUdpPort, String username, List<Neighbour> existingNodes) {
        //send and receive - to stay synced with the bootstrap server
        String regMsg = UdpMsgBuilder.buildRegisterMsg(nodeAddress.getHostAddress(), nodeUdpPort, username);
        DatagramPacket responsePacket =  UdpHelper.sendAndReceiveMessage(regMsg, bootstrapAddress, bootstrapServerPort,
                nodeUdpPort, 2000);

        //process response
        if (responsePacket.getAddress() == null) { // no response from bootstrap server
            return false;
        }
        MessageDecoder messageDecoder = MessageDecoder.getInstance();
        MessageObject response;
        try {
            //here the data looks like: length REGOK no_nodes IP_1 port_1 IP_2 port_2
            response = messageDecoder.decode(responsePacket.getData(), responsePacket.getLength());
        } catch (ErroneousResponseException e) { // error response from bootstrap server
            logger.error(e.getMessage());
            return false;
        }
        existingNodes.addAll(response.getExistingNodes());
        logger.debug("{} existing node addresses received from the bootstrap server", existingNodes.size());
        return true;
    }

}
