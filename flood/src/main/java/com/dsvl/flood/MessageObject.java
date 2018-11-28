package com.dsvl.flood;

import java.util.List;

public class MessageObject {
    private String msgType;
    private List<Neighbour> existingNodes;
    private Neighbour joinRequester;

    public String getMsgType() {
        return msgType;
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
