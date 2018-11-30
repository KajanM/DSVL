package com.dsvl.flood.controller;

import com.dsvl.flood.Node;
import com.dsvl.flood.model.NodeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeDetailsController {

    private Node node;

    @GetMapping("/node-details")
    public NodeDetails getNodeDetails() {
        return new NodeDetails(
                node.getBootstrapServerAddress(),
                node.getStatus(),
                node.getNodeAddress(),
                node.getTcpPort(),
                node.getNodeUdpPort()
        );
    }


    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
