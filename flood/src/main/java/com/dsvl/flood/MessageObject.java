package com.dsvl.flood;

import java.util.List;

public class MessageObject {
    private String msgType;
    private List<Neighbour> existingNodes;
    private Neighbour joinRequester;

    private String file_name;
    private int hops;
    private String ip;
    private int tcpPort;
    private int no_of_results;
    private String search_ip;
    private int search_udp_Port;
    private int search_tcp_Port;
    private int senderPort;
    private String senderIP;
    private String search_result_ip;
    private int search_result_tcp_Port;




    public String getSearch_ip() {
        return search_ip;
    }

    public int getSearch_udp_Port() {
        return search_udp_Port;
    }

    public void setSearch_ip(String search_ip) {
        this.search_ip = search_ip;
    }

    public void setSearch_udp_Port(int search_udp_Port) {
        this.search_udp_Port = search_udp_Port;
    }




    public String getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }


    public int getSenderPort() {
        return senderPort;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }



    public String getMsgType() {
        return msgType;
    }

    public void setFile_name(String file_name){
        this.file_name=file_name;
    }
    public String getFile_name(){
        return file_name;
    }
    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public List<Neighbour> getExistingNodes() {
        return existingNodes;
    }

    public void setExistingNodes(List<Neighbour> existingNodes) {
        this.existingNodes = existingNodes;
    }

    public Neighbour getJoinRequester() {
        return joinRequester;
    }

    public void setJoinRequester(Neighbour joinRequester) {
        this.joinRequester = joinRequester;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getNo_of_results() {
        return no_of_results;
    }

    public void setNo_of_results(int no_of_results) {
        this.no_of_results = no_of_results;
    }

    public int getSearch_tcp_Port() {
        return search_tcp_Port;
    }

    public void setSearch_tcp_Port(int search_tcp_Port) {
        this.search_tcp_Port = search_tcp_Port;
    }

    public String getSearch_result_ip() {
        return search_result_ip;
    }

    public void setSearch_result_ip(String search_result_ip) {
        this.search_result_ip = search_result_ip;
    }

    public int getSearch_result_tcp_Port() {
        return search_result_tcp_Port;
    }

    public void setSearch_result_tcp_Port(int search_result_tcp_Port) {
        this.search_result_tcp_Port = search_result_tcp_Port;
    }
}
