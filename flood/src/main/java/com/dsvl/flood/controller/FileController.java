package com.dsvl.flood.controller;

import com.dsvl.flood.File;
import com.dsvl.flood.Node;
import com.dsvl.flood.model.Log;
import com.dsvl.flood.service.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Slf4j
public class FileController {

    private Node node;
    private LogRepository logRepository;
    private RestTemplate restTemplate;

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


        File file;
        for (String name : names.split(",")) {
            file = new File(name);
            node.deleteFile(file);
        }
        System.out.println(names);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file/{fileName}")
    public ByteArrayResource downloadFile(@PathVariable(value = "fileName") String fileName, HttpServletRequest httpServletRequest) throws IOException {
        String name = fileName + ".txt";
        Log dbLog = new Log(
                httpServletRequest.getRemoteAddr() + ":" + httpServletRequest.getRemotePort(),
                "this", "TCP", "get file " + name
        );
        logRepository.save(dbLog);

        String toWrite = "This is a temporary file created on the fly: " + name;
        java.io.File tmpFile = java.io.File.createTempFile(fileName, ".txt");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(toWrite);
        writer.close();

        Path path = Paths.get(tmpFile.getAbsolutePath());
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    @PostMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestBody String url) {
        ByteArrayResource body = restTemplate.getForEntity(url, ByteArrayResource.class).getBody();
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_XML)
                .body(body);
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
