package com.dsvl.flood;

import java.util.List;

public class MessageObject {
    private String msgType;
    private List<Neighbour> existingNodes;
    private Neighbour joinRequester;
    private String file_name;
    private int hops;
//    private Integer tcp_port;
//    private String sendersAddress;

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
}
