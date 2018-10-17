package com.dsvl.flood.controller;

import com.dsvl.flood.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search")
public class SearchController {

    private Node node;

    // TODO: kasthuri, convert this to post mapping
    @GetMapping("/{file-name}")
    public String search(@PathVariable(value = "file-name") String fileName, Model model) {
        model.addAttribute("results", node.search(fileName));
        return "results";
    }

    @Autowired
    public void setNode(Node node) {
        this.node = node;
    }
}
