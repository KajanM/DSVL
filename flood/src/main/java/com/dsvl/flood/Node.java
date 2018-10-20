package com.dsvl.flood;

import com.dsvl.flood.service.JoinService;
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
     * {@code Boolean} value indicating if this {@code Nods} is registered
     * with the bootstrap server or not.
     */
    private Boolean isRegistered = false;

    /**
     * {@code Boolean} value indicating if this {@code Nods} is connected
     * to the network or not.
     */
    private Boolean connected = false;

    /**
     * The IP address of the {@code Node}.
     * Eagerly initialized
     */
    private final InetAddress nodeAddress;

    /**
     * The IP address of the bootstrap server.
     * Eagerly initialized
     */
    private final InetAddress bootstrapServerAddress;

    /**
     * List of files that this {@code Node} has
     */
    private final List<File> files;

    /**
     * List of neighbours that exists in the network but this {@code Node} has not directly connected to
     */
    private final List<Neighbour> existingNodes;

    /**
     * List of neighbours that this {@code Node} has directly connected to
     */
    private final List<Neighbour> neighbours;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private JoinService joinService;

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

        existingNodes = new ArrayList<>();
        neighbours = new ArrayList<>();

    }

    private void initializeFiles() {
        files.add(new File("Lord of the Rings"));
        files.add(new File("Adventures of Tintin"));
        files.add(new File("Hacking for Dummies"));
        files.add(new File("Super Mario"));
    }

    public boolean register() {
        isRegistered = registerService.register(bootstrapServerAddress, bootstrapServerPort, nodeAddress, nodeUdpPort,
                name, existingNodes);
        return isRegistered;
    }

    public boolean joinNetwork(List<Neighbour> existingPeers) {
        List<Neighbour> peers = new ArrayList<>();
        peers.addAll(existingPeers);

        /*
        * Tries to connect to a random peer, if successful removes from the local list
        */
        while (!peers.isEmpty()) {
            int peerIndex  = (int)(Math.random() * peers.size()); // 0 <= peerIndex < (neighbour list length)
            Neighbour peer = peers.get(peerIndex);
            boolean joinSuccessful = joinService.join(peer.getAddress(), peer.getPort(), nodeAddress, nodeUdpPort);
            if (joinSuccessful) {
                neighbours.add(peer);
            } else {
                peers.remove(peerIndex);
            }
            if (neighbours.size() > 1){
                logger.info("Successfully joined the network");
                return true;
            }
        }
        if (neighbours.size() == 1) {
            logger.warn("Joined the network via only one peer");
            return true;
        }
        return false;
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

    public Boolean isRegistered() {
        return isRegistered;
    }

    public Integer getNodeUdpPort() {
        return nodeUdpPort;
    }

    public void setRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }
}
