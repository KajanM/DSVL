package com.dsvl.flood;

import com.dsvl.flood.service.RegisterService;
import com.dsvl.flood.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
    private final int nodeUdpPort;
    private final String name;
    private final int nodeTcpPort;

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

    /**
     * List of files that this {@code Node} has
     */
    private final List<File> files;

    private final List<Neighbour> neighbours;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private SearchService searchService;

    @Autowired
    public Node(@Value("${bootstrap-server.address}") String bsIpValue, @Value("${bootstrap-server.port}") int bsPort, @Value("${name}") String name, @Value("${server.port}") int nodeTcpPort, @Value("${node.port}") int nodeUdpPort) throws UnknownHostException {
        bootstrapServerPort = bsPort;
        this.name = name;
        this.nodeTcpPort = nodeTcpPort;

        bootstrapServerAddress = InetAddress.getByName(bsIpValue);
        nodeAddress = InetAddress.getByName("localhost");

        // TODO: make the node's port a random value
        this.nodeUdpPort = nodeUdpPort;

        files = new ArrayList<>();
        initializeFiles();

        neighbours = new ArrayList<>();

    }

    private void initializeFiles() {
        files.add(new File("Lord of the Rings"));
        files.add(new File("Adventures of Tintin"));
        files.add(new File("Hacking for Dummies"));
        files.add(new File("Super Mario"));
    }

    public boolean register() {
        return registerService.register(bootstrapServerAddress, nodeAddress, bootstrapServerPort, nodeUdpPort, name);
    }

    public List<File> search(String fileName) {
        List<File> results = searchInLocalStore(fileName);

        if (results.isEmpty()) {
            results = searchService.search(fileName, neighbours, nodeAddress, nodeTcpPort);
        }

        return results;
    }

    private List<File> searchInLocalStore(String fileName) {
        // TODO: thilan, implementation to check if query file is in local storage
        List<File> results = new ArrayList<>();
        files.stream()
                .filter((file) -> file.getFileName().contains(fileName))
                .forEach(results::add);
        return  results;
    }
}
