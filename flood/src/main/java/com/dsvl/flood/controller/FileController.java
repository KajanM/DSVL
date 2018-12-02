package com.dsvl.flood.controller;

import com.dsvl.flood.File;
import com.dsvl.flood.Node;
import com.dsvl.flood.model.Log;
import com.dsvl.flood.service.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FileController {

    private Node node;
    private LogRepository logRepository;

    @GetMapping("/files")
    public List<File> getFiles() {
        return node.getFiles();
    }

    @PostMapping("/files/add")
    public ResponseEntity<?> addFiles(@RequestBody String names) {
        Log log = new Log(
                "this",
                "this",
                "TCP",
                "add following files " + names
        );
        logRepository.save(log);

        File file;
        for (String name : names.split(",")) {
            file = new File(name);
            node.addFile(file);
        }
        System.out.println(names);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/files/delete")
    public ResponseEntity<?> deleteFiles(@RequestBody String names) {
        Log log = new Log(
                "this",
                "this",
                "TCP",
                "delete following files " + names
        );
        logRepository.save(log);

        //Arrays.asList(names.split(",")).forEach(
        //        name -> {
        //            File file = new File(name);
        //            node.deleteFile(file);
        //        }
        //);
        File file;
        for (String name : names.split(",")) {
            file = new File(name);
            node.deleteFile(file);
        }
        System.out.println(names);
        return ResponseEntity.ok().build();
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
}
