package com.dsvl.flood.controller;

import com.dsvl.flood.Node;
import com.dsvl.flood.model.Data;
import com.dsvl.flood.model.NodeDetails;
import com.dsvl.flood.service.LogRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ServerSentEventController {

    private Node node;
    private LogRepository logRepository;

    @CrossOrigin
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        Data data = new Data();

        data.setNodeDetails(new NodeDetails(
                node.getBootstrapServerAddress(),
                node.getStatus(),
                node.getNodeAddress(),
                node.getTcpPort(),
                node.getNodeUdpPort()
        ));
        data.setLogs(Lists.newArrayList(logRepository.findAll()));
        data.setFiles(node.getFiles());

        data.setNeighbours(node.getNeighbours());

        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(data)
                            .id(String.valueOf(i))
                            .name("data");
                    emitter.send(event);
                    Thread.sleep(3000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
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
