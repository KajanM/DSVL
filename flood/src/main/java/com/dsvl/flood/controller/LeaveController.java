package com.dsvl.flood.controller;

import com.dsvl.flood.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaveController {
    private Node node;

    @PostMapping("/leave")
    public ResponseEntity<?> leave() {
        node.triggerLeave();
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
