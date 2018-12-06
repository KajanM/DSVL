package com.dsvl.flood.controller;

import com.dsvl.flood.Node;
import com.dsvl.flood.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResultsController {

    @GetMapping("/results")
    public List<Result> results() {
        return Node.latestSearchResults;
    }

}
