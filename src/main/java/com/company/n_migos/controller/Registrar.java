package com.company.n_migos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Registrar {
    @GetMapping("/registro")
    public String registro(){
        return "registro";
    }
}
