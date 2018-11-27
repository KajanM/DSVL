package com.dsvl.flood;

import java.util.List;

public class MessageObject {
    List<Neighbour> existingNodes;

    public List<Neighbour> getExistingNodes() {
        return existingNodes;
    }

    public void setExistingNodes(List<Neighbour> existingNodes) {
        this.existingNodes = existingNodes;
    }
}
