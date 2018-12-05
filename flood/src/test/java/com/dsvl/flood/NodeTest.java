package com.dsvl.flood;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.*;

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