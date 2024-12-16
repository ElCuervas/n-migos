package com.company.n_migos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class menu {
    @GetMapping
    public String getMenuPage() {
        return "menu";
    }
}


