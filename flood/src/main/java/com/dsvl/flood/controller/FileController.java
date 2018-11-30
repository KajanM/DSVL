package com.dsvl.flood.controller;

import com.dsvl.flood.File;
import com.dsvl.flood.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {

    private Node node;

    @GetMapping("/files")
    public List<File> getFiles() {
       return node.getFiles();
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
