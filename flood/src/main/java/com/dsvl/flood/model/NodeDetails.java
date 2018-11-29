package com.dsvl.flood.model;

public class NodeDetails {

    private String bootstrapServerAddress;
    private String status;
    private String ipAddress;
    private int tcpPort;
    private int udpPort;

    public NodeDetails() {
    }

    public NodeDetails(String bootstrapServerAddress, String status, String ipAddress, int tcpPort, int udpPort) {
        this.bootstrapServerAddress = bootstrapServerAddress;
        this.status = status;
        this.ipAddress = ipAddress;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public String getBootstrapServerAddress() {
        return bootstrapServerAddress;
    }

    public void setBootstrapServerAddress(String bootstrapServerAddress) {
        this.bootstrapServerAddress = bootstrapServerAddress;
    }

    @Override
    public String toString() {
        return "NodeDetails{" +
                "bootstrapServerAddress='" + bootstrapServerAddress + '\'' +
                ", status='" + status + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", tcpPort=" + tcpPort +
                ", udpPort=" + udpPort +
                '}';
    }
}
