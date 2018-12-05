package com.dsvl.flood;

import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UdpServerTest {

    @Test
    public void run() {


        byte[] buf = "0047 SER 127.0.0.1 45555 Lord_of_the_rings_2_hacking 0".getBytes();
        DatagramPacket packet = null;
        try {

            packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), 44444);
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
    public void run1() {

    }

    @Test
    public void getIpString() throws SocketException, UnknownHostException {
        InetAddress address;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            address = socket.getLocalAddress();
        }

    }

    @Test
    public void testNullIteration() {
        List<String> emptyList = new ArrayList<>();
        for (String s :
                emptyList) {
            System.out.println("hello");
        }
    }
}