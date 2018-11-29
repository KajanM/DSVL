package com.dsvl.flood.controller;

import com.dsvl.flood.Node;
import com.dsvl.flood.model.NodeDetails;
import com.dsvl.flood.service.LogRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    private Node node;
    private LogRepository logRepository;

    @CrossOrigin
    @GetMapping(path = "/data")
    public Map<String, Object> getData() {

        Map<String, Object> data = new HashMap<>();
        NodeDetails nodeDetails = new NodeDetails(
                node.getBootstrapServerAddress(),
                node.getStatus(),
                node.getNodeAddress(),
                node.getTcpPort(),
                node.getNodeUdpPort()
        );

        data.put("nodeDetails", nodeDetails);
        data.put("logs", Lists.newArrayList(logRepository.findAll()));
        data.put("files", node.getFiles());
        data.put("neighbours", node.getNeighbours());

        return data;
    }

    // ========== setter injection of dependencies ==========

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
