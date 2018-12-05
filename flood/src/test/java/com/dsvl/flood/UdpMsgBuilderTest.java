package com.dsvl.flood;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UdpMsgBuilderTest {

    @Test
    public void buildSearchMsg() {
        // length SER IP port file_name hops
        String serMsg = UdpMsgBuilder.buildSearchMsg("127.0.0.1", 45555, "Lord of the rings", 0);
        assertEquals("0044 SER 127.0.0.1 45555 Lord of the rings 0", serMsg);
    }

    @Test
    public void buildRegisterMsg() {
        //length REG IP_address port_no username
        String regMsg = UdpMsgBuilder.buildRegisterMsg("127.0.0.1", 45555, "dsvl");
        assertEquals("0029 REG 127.0.0.1 45555 dsvl", regMsg);
    }

    @Test
    public void buildJoinMsg() {
        //length JOIN IP_address port_no
        String joinMsg = UdpMsgBuilder.buildJoinMsg("127.0.0.1", 45555);
        assertEquals("0025 JOIN 127.0.0.1 45555", joinMsg);
    }

    @Test
    public void buildLeaveMsg() {
        //length LEAVE IP_address port_no 2 IP_address port_no IP_address port_no
        List<Neighbour> myNeighbours = new ArrayList<>();
        try {
            myNeighbours.add(new Neighbour(InetAddress.getByName("197.8.9.1"), 47657));
            myNeighbours.add(new Neighbour(InetAddress.getByName("197.8.9.2"), 47657));
        } catch (UnknownHostException e) {
            //ignore
        }
        String leaveMsg = UdpMsgBuilder.buildLeaveMsg("127.0.0.1", 45555, myNeighbours);
        assertEquals("0060 LEAVE 127.0.0.1 45555 2 197.8.9.1 47657 197.8.9.2 47657", leaveMsg);
    }

    @Test
    public void buildUnregisterMsg() {
        //length UNREG IP_address port_no username
        String unregMsg = UdpMsgBuilder.buildUnregisterMsg("127.0.0.1", 45555, "dsvl");
        assertEquals("0031 UNREG 127.0.0.1 45555 dsvl", unregMsg);
    }
}