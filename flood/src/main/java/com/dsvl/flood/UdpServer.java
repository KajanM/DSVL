package com.dsvl.flood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This {@code Component} starts with the application and attempts
 * to register with the bootstrap server.
 * <br>
 * If register attempt is failed for some reason
 * this will reattempt to register after waiting for five seconds.
 * <br>
 * Once registration is successful, it then starts listening to incoming
 * UDP messages forever.
 * This {@code CommandLineRunner} exists with the main application
 */
@Component
public class UdpServer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);

    @Autowired
    private Node node;

    @Override
    public void run(String... args) {

        while (!node.isRegistered()) {
            logger.info("Attempting to register with the bootstrap server");

            if (node.register()) break;

            // TODO: instead of sleeping for fixed 5 seconds apply some incremental logic
            logger.info("Sleeping for 5 seconds");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //ignore
            }
        }

        // Create an ever running UDP receiving socket
        try (DatagramSocket socket = new DatagramSocket(node.getNodeUdpPort())) {
            logger.debug("UDP server started for incoming messages at port {}", node.getNodeUdpPort());
            byte[] buffer;
            while (true) {
                buffer = new byte[65536];
                DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(incomingPacket);
                if (incomingPacket.getData().length != 0) {
                    // TODO: process incoming UDP message
                    String receivedData = new String(incomingPacket.getData(), 0, incomingPacket.getLength());
                    logger.info("Received UDP message from {}:{} {}", incomingPacket.getAddress().getHostAddress(), incomingPacket.getPort(), receivedData);
                }
            }
        } catch (SocketException e) {
            logger.error("Unable to open UDP socket for receiving", e);
        } catch (IOException e) {
            logger.error("Unable to receive UDP message", e);
        }

    }
}
