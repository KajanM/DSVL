package com.dsvl.flood;

import com.dsvl.flood.Constants.Status;
import com.dsvl.flood.model.ForwardedQuery;
import com.dsvl.flood.model.Result;
import com.dsvl.flood.service.JoinService;
import com.dsvl.flood.service.LeaveService;
import com.dsvl.flood.service.RegisterService;
import com.dsvl.flood.service.SearchService;
import com.dsvl.flood.service.UnregisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.SocketUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

import static com.dsvl.flood.Constants.Status.JOINED;
import static com.dsvl.flood.Constants.Status.NOT_REGISTERED;
import static com.dsvl.flood.Constants.Status.UNREGISTERED_AND_DISCONNECTED;


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
    /**
     * Node's listening UDP port
     */
    private final int nodeUdpPort;
    private final String name;
    private final int nodeTcpPort;

    /**
     * {@code Boolean} value indicating if this {@code Node} is registered
     * with the bootstrap server or not.
     */
    private Boolean isRegistered = false;

    /**
     * {@code Boolean} value indicating if this {@code Node} is leaving the network.
     */
    public volatile Boolean isLeaving = false;

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
    private List<File> files;

    /**
     * List of neighbours that exists in the network but this {@code Node} has not directly connected to
     */
    private final List<Neighbour> existingNodes;

    /**
     * List of neighbours that this {@code Node} has directly connected to
     */
    private static volatile List<Neighbour> neighbours;
    private static Map<Integer, HashSet> map;
    public static volatile ArrayList<Result> latestSearchResults;
    public static volatile List<ForwardedQuery> forwardedQueries;

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
    private UnregisterService unregisterService;

    @Autowired
    public Node(@Value("${bootstrap-server.address}") String bsIpValue,
                @Value("${bootstrap-server.port}") int bsPort,
                @Value("${name}") String name,
                @Value("${server.port}") int nodeTcpPort,
                @Value("${node.port}") int nodeUdpPort) throws UnknownHostException, SocketException {
        bootstrapServerPort = bsPort;
        this.name = name;
        this.nodeTcpPort = nodeTcpPort;

        bootstrapServerAddress = InetAddress.getByName(bsIpValue);
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            nodeAddress = socket.getLocalAddress();
        }

        this.nodeUdpPort = nodeUdpPort;

        latestSearchResults = new ArrayList<>();
        files = new ArrayList<>();
        initializeFiles();
        update_table();
        existingNodes = new ArrayList<>();
        neighbours = new ArrayList<>();
        status = NOT_REGISTERED;
        forwardedQueries = new ArrayList<>();
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
            status = JOINED;
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
            boolean joinSuccessful = joinService.join(peer.getIpAddress(), peer.getUdpPort(), nodeAddress, nodeUdpPort);
            if (joinSuccessful) {
                neighbours.add(peer);
                peer.settTL(5);
                logger.info("New node added as neighbor, IP address: {}, port: {}", peer.getIpAddress(), peer.getUdpPort());
            }
            peers.remove(peerIndex);
            if (neighbours.size() == 2){
                status = JOINED;
                logger.info("Successfully joined the network");
                return true;
            }
        }
        if (neighbours.size() > 0) {
            status = JOINED;
            logger.info("Joined the network");
            return true;
        }
        return false;
    }

    public void join(List<Neighbour> someNodes) {
        logger.info("Trying to join the network");
        List<Neighbour> peers = new ArrayList<>(someNodes);

        /*
         * Tries to connect to a random peer, if successful removes from the local list
         */
        int tempUdpPort = SocketUtils.findAvailableUdpPort();
        while (!peers.isEmpty() && neighbours.size() < 5) {
            int peerIndex = (int) (Math.random() * peers.size()); // 0 <= peerIndex < (neighbour list length)
            Neighbour peer = peers.get(peerIndex);
            boolean joinSuccessful = joinService.join(peer.getIpAddress(), peer.getUdpPort(), nodeAddress, tempUdpPort);
            if (joinSuccessful) {
                neighbours.add(peer);
                logger.info("New node added as neighbor, IP address: {}, port: {}", peer.getIpAddress(), peer.getUdpPort());
            }
        }
    }


    public void sendPingMessage() {
        while (true) {
            List<Neighbour> valuesToRemove = new ArrayList<>();
            for (Neighbour n : neighbours) {
                String key = "PNG";
                String myip = nodeAddress.getHostAddress();
                String myport = String.valueOf(nodeUdpPort);
                int tTL = n.gettTL();

                String query = key + " " + myip + " " + myport;
                String length = String.format("%04d", query.length() + 4);
                query = length + " " + query;

                if (tTL == 0) {
                    valuesToRemove.add(n);
                    logger.info("Neighbor eliminated, IP address: {}, port: {}", n.getIpAddress(), n.getUdpPort());
                } else {
                    n.settTL(tTL - 1);
                    UdpHelper.sendMessage(query, n.getIpAddress(), n.getUdpPort());
                    logger.info("Sent ping message , IP address: {}, port: {}", n.getIpAddress(), n.getUdpPort());
                }
            }
            neighbours.removeAll(valuesToRemove);

            if (neighbours.size() == 0) {
                logger.info("No neighbours to ping: ");
            }

            try {
                Thread.sleep(1200000); // Waits for 2 minutes
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public List<File> search(MessageObject msgObject) {
        List<File> results = searchInLocalStore(msgObject.getFile_name());
        //TODO: get fixed hop count from property file
        //if (nodeAddress.equals(msgObject.getSearch_ip()) && getNodeUdpPort() == msgObject.getSearch_udp_Port()) {
        //    results.forEach(file -> {
        //        Result result = new Result(file.getFileName(), getNodeAddress() + ":" + getTcpPort() + "/file/" + file.getFileName(), msgObject.getHops());
        //
        //        Node.latestSearchResults.add(result);
        //    });
        //}

        msgObject.setHops(msgObject.getHops() - 1);

        if (msgObject.getHops() > 0) {
            try {
                searchService.search(msgObject, neighbours, nodeAddress, nodeTcpPort);
            } catch (Exception e) {
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

        ArrayList<File> uniquefiles = new ArrayList<File>();// unique
        for (File file : files) {
            if (!uniquefiles.contains(file)) {
                uniquefiles.add(file);
            }
        }
        this.files=uniquefiles;

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

    public boolean leaveNetwork() {
        logger.info("Preparing to leave the network");

        this.isLeaving = true; //to break the ever running server while loop

        //releasing the ever running udp port
        int tempUdpPort = SocketUtils.findAvailableUdpPort();
        UdpHelper.sendMessage("", nodeAddress, nodeUdpPort, tempUdpPort);

        unregister();

        if(neighbours.isEmpty()) {
            logger.info("I am the only node in the network. Leaving gracefully.");
            status = UNREGISTERED_AND_DISCONNECTED;
            return true;
        }
        List<Neighbour> myNeighbours = new ArrayList<>();
        myNeighbours.addAll(neighbours);

        for (Neighbour neighbour : neighbours) {
            myNeighbours.remove(neighbour); //so the receiver address will not be added to the leave msg
            boolean leaveSuccessful = leaveService.leave(neighbour.getIpAddress(), neighbour.getUdpPort(),
                    nodeAddress, nodeUdpPort, myNeighbours);
            myNeighbours.add(neighbour);
            if (leaveSuccessful) {
                logger.info("Informed neighbour {}:{} about leaving", neighbour.getIpAddress(), neighbour.getUdpPort());
            } else {
                logger.info("Could not properly inform neighbour {}:{} about leaving", neighbour.getIpAddress(), neighbour.getUdpPort());
            }
        }
        neighbours.clear();
        if (neighbours.size() == 0) {
            logger.info("Finished informing the neighbours. Leaving gracefully.");
            status = UNREGISTERED_AND_DISCONNECTED;
            return true;
        }
        status = UNREGISTERED_AND_DISCONNECTED;
        return false;
    }

    private boolean unregister() {
        boolean unregistered = unregisterService.unregister(bootstrapServerAddress, bootstrapServerPort,
                nodeAddress, nodeUdpPort, name);
        if(unregistered) {
            logger.info("Successfully unregistered from the bootstrap server");
            return true;
        }
        logger.info("Not able to unregister from bootstrap server. Skipping..");
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

    /**
     * Returns the IP Address of the current Node
     * @return
     */
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
        update_table();
    }

    public void deleteFile(File file) {
        files.remove(file);
    }

}
