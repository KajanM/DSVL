package com.dsvl.flood;

import com.dsvl.flood.Constants.Status;
import com.dsvl.flood.service.JoinService;
import com.dsvl.flood.service.LeaveService;
import com.dsvl.flood.service.RegisterService;
import com.dsvl.flood.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static com.dsvl.flood.Constants.Status.NOT_REGISTERED;


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
    private static Map<Integer, HashSet> map;
    public static ArrayList<String> latestSearchResults;

    /**
     * Used to update UI
     */
    private Status status;


    @Autowired
    private RegisterService registerService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private SearchService searchService;

    @Autowired
    public Node(@Value("${bootstrap-server.address}") String bsIpValue,
                @Value("${bootstrap-server.port}") int bsPort,
                @Value("${name}") String name,
                @Value("${server.port}") int nodeTcpPort,
                @Value("${node.port}") int nodeUdpPort) throws UnknownHostException {
        bootstrapServerPort = bsPort;
        this.name = name;
        this.nodeTcpPort = nodeTcpPort;

        bootstrapServerAddress = InetAddress.getByName(bsIpValue);
        nodeAddress = InetAddress.getByName("localhost");

        this.nodeUdpPort = nodeUdpPort;

        latestSearchResults=new ArrayList<>();
        files = new ArrayList<>();
        initializeFiles();
        update_table();
        existingNodes = new ArrayList<>();
        neighbours = new ArrayList<>();
        status = NOT_REGISTERED;

    }

    private void initializeFiles() {
        files.add(new File("Lord of the Rings"));
        files.add(new File("Adventures of Tintin"));
        files.add(new File("Hacking for Dummies"));
        files.add(new File("Super Mario"));
        files.add(new File("Super Mario 2"));
    }

    public boolean register() {
        isRegistered = registerService.register(bootstrapServerAddress, bootstrapServerPort, nodeAddress, nodeUdpPort,
                name, existingNodes);
        return isRegistered;
    }

    public boolean joinNetwork(List<Neighbour> existingNodes) {
        logger.info("Trying to join the network");
        if (existingNodes.isEmpty()) {
            logger.info("I am the only node in the network");
            return true;
        }
        List<Neighbour> peers = new ArrayList<>(existingNodes);

        /*
         * Tries to connect to a random peer, if successful removes from the local list
         */
        while (!peers.isEmpty()) {
            int peerIndex = (int) (Math.random() * peers.size()); // 0 <= peerIndex < (neighbour list length)
            Neighbour peer = peers.get(peerIndex);
            boolean joinSuccessful = joinService.join(peer.getAddress(), peer.getPort(), nodeAddress, nodeUdpPort);
            if (joinSuccessful) {
                neighbours.add(peer);
                logger.info("New node added as neighbor, IP address: {}, port: {}", peer.getAddress(), peer.getPort());
            }
            peers.remove(peerIndex);
            if (neighbours.size() == 2) {
                logger.info("Successfully joined the network");
                return true;
            }
        }
        if (neighbours.size() > 0) {
            logger.info("Joined the network");
            return true;
        }
        return false;
    }


    public List<File> search(MessageObject msgObject) throws NullPointerException {
        msgObject.setHops(msgObject.getHops()-1);
        List<File> results = searchInLocalStore(msgObject.getFile_name());

        if (msgObject.getHops()>0) {
            try{
                searchService.search(msgObject, neighbours, nodeAddress, nodeTcpPort);
            }catch (Exception e){
                logger.error("Unable to propogate search to neighbour nodes", e);
            }
        }
        return results;
    }

    private List<File> searchInLocalStore(String fileName) {
        ArrayList<Integer> file_indexes = getFileIndexes(fileName);
        List<File> results = new ArrayList<>();
        for (Integer file_index : file_indexes) {
            results.add(files.get(file_index));
        }
        return results;

    }

    private ArrayList<Integer> getFileIndexes(String fileName) {
        Set<String> myset = new HashSet<>();
        String[] s = fileName.toLowerCase().split("_");
        for (String s1 : s) {
            myset.add(s1);
        }
        int highest = 0;
        ArrayList<Integer> file_indexes = new ArrayList<Integer>();
        for (int i = 0; i < files.size(); i++) {
            Set<String> intersection = new HashSet<String>(myset);
            intersection.retainAll(map.get(i));
            if (intersection.size() > highest) {
                file_indexes.add(i);
            }
        }
        return file_indexes;
    }

    private void update_table() {
        map = new HashMap<Integer, HashSet>();
        for (int i = 0; i < files.size(); i++) {
            Set<String> myset = new HashSet<>();
            String str = files.get(i).getFileName().toLowerCase();
            String s[] = str.split(" ");
            for (int j = 0; j < s.length; j++) {
                myset.add(s[j]);
            }
            map.put(i, (HashSet) myset);
        }
    }

    public boolean leaveNetwork(List<Neighbour> neighbours) {
        logger.info("Preparing to leave the network");
        if(neighbours.isEmpty()) {
            logger.info("I am the only node in the network. Leaving gracefully.");
            return true;
        }
        for(Neighbour neighbour: neighbours) {
            boolean leaveSuccessful = leaveService.leave(neighbour.getAddress(), neighbour.getPort(),
                    nodeAddress, nodeUdpPort);
            if (leaveSuccessful) {
                logger.info("Informed neighbour {}:{} about leaving", neighbour.getAddress(), neighbour.getPort());
            } else {
                logger.info("Could not properly inform neighbour {}:{} about leaving", neighbour.getAddress(), neighbour.getPort());
            }
            neighbours.remove(neighbour);
        }
        if (neighbours.size() == 0) {
            logger.info("Finished informing the neighbours. Leaving gracefully.");
            return true;
        }
        return false;
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

    public List<Neighbour> getExistingNodes() {
        return existingNodes;
    }

    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    public String getStatus() {
        return status.toString().toLowerCase();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNodeAddress() {
        return nodeAddress.getHostAddress();
    }

    public String getBootstrapServerAddress() {
        return bootstrapServerAddress.getHostAddress() + bootstrapServerPort;
    }

    public int getTcpPort() {
        return nodeTcpPort;
    }

    public List<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void deleteFile(File file) {
        files.remove(file);
    }

}
