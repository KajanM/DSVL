package com.dsvl.flood.controller;

import com.dsvl.flood.model.Log;
import com.dsvl.flood.service.LogRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

    private LogRepository logRepository;

    @GetMapping("/log")
    public List<Log> getLogs() {
        List<Log> logs = Lists.newArrayList(logRepository.findAll());
//        return Lists.newArrayList(logRepository.findAll());
        return logs;
    }

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
}
