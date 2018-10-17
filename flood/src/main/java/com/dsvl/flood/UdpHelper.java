package com.dsvl.flood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Helper class for UDP related actions
 *
 * @see #sendMessage(String, InetAddress, int, Integer)
 * @see #receiveMessage(int)
 */
public class UdpHelper {

    private static final Logger logger = LoggerFactory.getLogger(UdpHelper.class);

    /**
     * Send UDP message with a random port for UDP socket
     * @see #sendMessage(String, InetAddress, int, Integer)
     *
     * @param message
     * @param destinationAddress
     * @param destinationPort
     * @return whether the sending is succeeded or not
     */
    public static Boolean sendMessage(@NotNull String message, @NotNull InetAddress destinationAddress, @NotNull int destinationPort) {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
            logger.info("Sent UDP message to {}:{} {}", packet.getAddress().getHostAddress(), packet.getPort(), message);
        } catch (IOException e) {
            logger.error("unable to send the message", e);
            return false;
        }
        return true;
    }

    /**
     * Helper method to send a UDP message
     *
     * @param message
     * @param destinationAddress
     * @param destinationPort
     * @param nodePort
     * @return whether the action was succeeded or not
     */
    public static Boolean sendMessage(@NotNull String message, @NotNull InetAddress destinationAddress, @NotNull int destinationPort, Integer nodePort) {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);
        try (DatagramSocket socket = new DatagramSocket(nodePort)) {
            socket.send(packet);
            logger.info("Sent UDP message to {}:{} {}", packet.getAddress().getHostAddress(), packet.getPort(), message);
        } catch (IOException e) {
            logger.error("unable to send the message", e);
            return false;
        }
        return true;
    }

    /**
     * Helper method to receive a UDP message
     * Note that call to this method will block the executing thread until the message is received
     *
     * @return the received DatagramPacket
     */
    public static DatagramPacket receiveMessage(@NotNull int nodePort) {
        byte[] response = new byte[65536];

        DatagramPacket packet = new DatagramPacket(response, response.length);

        try (DatagramSocket socket = new DatagramSocket(nodePort)) {
            socket.receive(packet);
            String receivedData = new String(packet.getData(), 0, packet.getLength());
            logger.info("Received UDP message from {}:{} {}", packet.getAddress().getHostAddress(), packet.getPort(), receivedData);
        } catch (IOException e) {
            logger.error("Unable to receive response from bootstrap server", e);
            return null;
        }
        return packet;
    }
}
