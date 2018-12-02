package com.dsvl.flood;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
}