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

    private final int bootstrapServerPort;
    private final int nodePort;
    private final String name;

    /**
     * The IP address of the {@code Node}.
     * Eagerly initialized}
     */
    private final InetAddress nodeAddress;

    /**
     * The IP address of the bootstrap server.
     * Eagerly initialized}
     */
    private final InetAddress bootstrapServerAddress;

    @Autowired
    private RegisterService registerService;

    @Autowired
    public Node(@Value("${bootstrap-server.address}") String bsIpValue, @Value("${bootstrap-server.port}") int bsPort, @Value("${name}") String name) throws UnknownHostException {
        bootstrapServerPort = bsPort;
        this.name = name;

        bootstrapServerAddress = InetAddress.getByName(bsIpValue);
        nodeAddress = InetAddress.getByName("localhost");

        // TODO: make the node's port a random value
        nodePort = 45555;
    }

    public boolean register() {
        return registerService.register(bootstrapServerAddress, nodeAddress, bootstrapServerPort, nodePort, name);
    }
}
