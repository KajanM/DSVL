package com.dsvl.flood;

import com.dsvl.flood.model.Log;
import com.dsvl.flood.service.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.*;

/**
 * Helper class for UDP related actions
 *
 * @see #sendMessage(String, InetAddress, int, Integer)
 * @see #receiveMessage(int)
 */
@SuppressWarnings("ALL")
@Component
public class UdpHelper {

    private static final Logger logger = LoggerFactory.getLogger(UdpHelper.class);
    private static LogRepository logRepository;

    @Autowired
    public UdpHelper(LogRepository logRepository) {
        UdpHelper.logRepository = logRepository;
    }

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
            Log log = new Log(
                    "this",
                    destinationAddress.getHostAddress() + ":" + destinationPort, "UDP", message
            );
            logRepository.save(log);
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
    public static Boolean sendMessage(@NotNull String message, @NotNull InetAddress destinationAddress, @NotNull int destinationPort, @NotNull Integer nodePort) {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);
        try (DatagramSocket socket = new DatagramSocket(nodePort)) {
            Log log = new Log(
                    "this",
                    destinationAddress.getHostAddress() + ":" + destinationPort, "UDP", message
            );
            logRepository.save(log);
            socket.send(packet);
            logger.info("Sent UDP message to {}:{} {}", packet.getAddress().getHostAddress(), packet.getPort(), message);
        } catch (IOException e) {
            logger.error("unable to send the message", e);
            return false;
        }
        return true;
    }

    /**
     * To keep the bootstrap server and this node synced, had to include both send and receive in the same method
     *
     * @param message
     * @param destinationAddress
     * @param destinationPort
     * @param nodePort
     * @return the response DatagramPacket
     */
    public static DatagramPacket sendAndReceiveMessage(@NotNull String message,
                                                       @NotNull InetAddress destinationAddress, @NotNull int destinationPort,
                                                       @NotNull Integer nodePort, @NotNull int timeOutInMilliSecond) {
        byte[] buf = message.getBytes();
        byte[] response = new byte[65536];
        DatagramPacket requestPacket = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);
        DatagramPacket responsePacket = new DatagramPacket(response, response.length);
        try (DatagramSocket socket = new DatagramSocket(nodePort)) {
            //send
            Log log = new Log(
                    "this",
                    destinationAddress.getHostAddress() + ":" + destinationPort, "UDP", message
            );
            logRepository.save(log);
            socket.send(requestPacket);
            logger.info("Sent UDP message to {}:{} {}", requestPacket.getAddress().getHostAddress(), requestPacket.getPort(), message);

            //receive
            socket.setSoTimeout(timeOutInMilliSecond);
            socket.receive(responsePacket);
            String receivedData = new String(responsePacket.getData(), 0, responsePacket.getLength());
            log = new Log(
                    destinationAddress.getHostAddress() + ":" + destinationPort,
                    "this", "UDP", receivedData
            );
            logRepository.save(log);
            logger.info("Received UDP message from {}:{} {}", responsePacket.getAddress().getHostAddress(), responsePacket.getPort(), receivedData);
        }catch (SocketTimeoutException e) {
            logger.info("Timeout while waiting to receive UDP message");
        } catch (SocketException e) {
            logger.error("Error while sending or receiving packet", e);
        } catch (IOException e) {
            logger.error("unable to send the message", e);
        }
        return responsePacket;
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

    /**
     * Helper method to receive a UDP message
     * Note that call to this method will block the executing thread until the message is received
     *
     * @return the received DatagramPacket
     */
    public static DatagramPacket receiveMessage(@NotNull int nodePort, @NotNull int timeOutInMilliSecond) {
        byte[] response = new byte[65536];

        DatagramPacket packet = new DatagramPacket(response, response.length);

        try (DatagramSocket socket = new DatagramSocket(nodePort)) {
            socket.setSoTimeout(timeOutInMilliSecond);
            socket.receive(packet);
            String receivedData = new String(packet.getData(), 0, packet.getLength());
            logger.info("Received UDP message from {}:{} {}", packet.getAddress().getHostAddress(), packet.getPort(), receivedData);
        } catch (SocketTimeoutException e) {
            logger.info("Timeout while waiting to receive UDP message");
            return null;
        } catch (SocketException e) {
            logger.error("Unable to create a UDP receiving socket", e);
            return null;
        } catch (IOException e) {
            logger.error("Unable to receive UDP message", e);
            return null;
        }
        return packet;
    }
}
