package com.dsvl.flood.controller;

import com.dsvl.flood.MessageObject;
import com.dsvl.flood.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SearchController {

    private Node node;

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody String fileName) {
        Node.forwardedQueries.clear();
        Node.latestSearchResults.clear();

        MessageObject messageObject = new MessageObject();
        messageObject.setFile_name(fileName);
        messageObject.setSearch_ip(node.getNodeAddress());
        messageObject.setSearch_udp_Port(node.getNodeUdpPort());
        messageObject.setHops(5);
        node.search(messageObject);

        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
