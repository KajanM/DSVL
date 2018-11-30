package com.dsvl.flood.controller;

import com.dsvl.flood.Neighbour;
import com.dsvl.flood.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NeighbourController {

    private Node node;

    @GetMapping("/neighbours")
    public List<Neighbour> getNeighbours() {
        return node.getNeighbours();
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
