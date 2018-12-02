package com.dsvl.flood.controller;

import com.dsvl.flood.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResultsController {

    @GetMapping("/results")
    public List<Result> results() {
        List<Result> results = new ArrayList<>();
//        results.add(new Result("Panda", "https://www.concierto.cl/wp-content/uploads/2016/10/oso-panda-bambu.jpeg"));

        return results;
    }

}
