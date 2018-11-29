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
}
