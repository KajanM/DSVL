package com.dsvl.flood;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class NodeTest {

    @Test
    public void search() {
//        try {
//            Node node=new Node("127.0.0.1",55555,   "DSVL",4500,45555);
//            MessageObject msgObj=new MessageObject();
//            msgObj.setHops(1);
//            msgObj.setFile_name("of_mario");
//            List<File> search_results=node.search(msgObj);
//            for (File search_result : search_results) {
//                System.out.println(search_result.getFileName());
//            }
//            assertEquals("Lord of the Rings",search_results.get(0).getFileName());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        byte[] buf = "0047 SER 127.0.0.1 44444 Lord_of_the_rings_2_hacking 0".getBytes();
        DatagramPacket packet = null;
        try {

            packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), 44445);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
            System.out.printf("message sent");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep((long) 5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Ignore
    public void searchForExistingFileInTheNode() throws SocketException {
        MessageObject msgObj = new MessageObject();

        Node.latestSearchResults = new ArrayList<>();

        String ip = "127.0.0.1";
        int udpPort = Integer.parseInt("44444");
        String file_name = "Mario";
        String hops = "2";
        int hopss = Integer.parseInt(hops);
        msgObj.setSearch_udp_Port(udpPort);
        msgObj.setSearch_ip(ip);
        msgObj.setFile_name(file_name);
        msgObj.setHops(hopss);

        try {
            Node node = new Node("127.0.0.1", 55555, "DSVL", 8080, 44444);
            List<File> search_results = node.search(msgObj);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchForNonExistingFileInTheNode() throws SocketException {
        MessageObject msgObj = new MessageObject();

        Node.latestSearchResults = new ArrayList<>();

        String ip = "127.0.0.1";
        int udpPort = Integer.parseInt("44444");
        String file_name = "Marioo";
        String hops = "2";
        int hopss = Integer.parseInt(hops);
        msgObj.setSearch_udp_Port(udpPort);
        msgObj.setSearch_ip(ip);
        msgObj.setFile_name(file_name);
        msgObj.setHops(hopss);

        try {
            Node node = new Node("127.0.0.1", 55555, "DSVL", 4500, 45555);
            List<File> search_results = node.search(msgObj);
            assertEquals(search_results.size(), 0);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    /**
     * @throws UnknownHostException
     * @throws SocketException
     */
    @Test
    @Ignore
    public void testRealIpAddress() throws UnknownHostException, SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
            Assert.assertEquals("10.10.2.160", ip);
        }
    }
}