package com.dsvl.flood.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaveController {
    @PostMapping("/leave")
    public ResponseEntity<?> leave() {
        // TODO: Subashini Call to LeaveService
        return ResponseEntity.ok().build();
    }
}
