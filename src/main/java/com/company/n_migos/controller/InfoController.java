package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.service.JuegoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InfoController {

    @Autowired
    private JuegoServicio servicioJuego;

    @GetMapping("/info")
    public String mostrarInfo(@RequestParam("id") Integer id, Model model) {
        Juego juego = servicioJuego.findById(id).get();
        // Agregar el juego al modelo para enviarlo a Thymeleaf
        model.addAttribute("juego", juego);

        return "info";
    }
}
