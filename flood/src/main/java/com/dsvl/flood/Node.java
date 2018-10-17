package com.dsvl.flood;

import com.dsvl.flood.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This {@code Component} represents the actual Node in the network.
 * <br/>
 * A node can
 * <ul>
 * <li>register itself with the bootstrap server</li>
 * <li>join with the other nodes in the network</li>
 * <li>query for files</li>
 * <li>propagate the file query to its neighbour nodes</li>
 * <li>download or upload files over TCP</li>
 * <li>unregister from the network</li>
 * </ul>
 */
@Component
public class Node {

    private static final Logger logger = LoggerFactory.getLogger(Node.class);

    @Value("${bootstrap-server.address}")
    private String bootstrapServerIpValue;

    @Value("${bootstrap-server.port}")
    private int bootstrapServerPort;

    // TODO: make the node's port a random value
    private int nodePort = 45555;

    @Value("${name}")
    private String name;

    /**
     * The IP address of the {@code Node}.
     * Lazily initialized by {@link #getNodeAddress()}
     */
    private InetAddress nodeAddress;

    /**
     * The IP address of the bootstrap server.
     * Lazily initialized by {@link #getBootstrapServerAddress()}
     */
    private InetAddress bootstrapServerAddress;

    @Autowired
    private RegisterService registerService;

    public Node() {
    }

    public boolean register() {
        try {
            return registerService.register(getBootstrapServerAddress(), getNodeAddress(), bootstrapServerPort, nodePort, name);
        } catch (UnknownHostException e) {
            logger.error("Failed creating InetAddresses", e);
            return false;
        }
    }

    public InetAddress getNodeAddress() throws UnknownHostException {
        if (nodeAddress == null) {
            // TODO: node address is always 127.0.0.1, this behavior should be changed
            nodeAddress = InetAddress.getByName("localhost");
        }
        return nodeAddress;
    }

    public InetAddress getBootstrapServerAddress() throws UnknownHostException {
        if (bootstrapServerAddress == null) {
            bootstrapServerAddress = InetAddress.getByName(bootstrapServerIpValue);
        }
        return bootstrapServerAddress;
    }
}
