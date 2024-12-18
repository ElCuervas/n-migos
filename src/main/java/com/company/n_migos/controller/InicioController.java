package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.service.JuegoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class InicioController {

    @Autowired
    private JuegoServicio servicioJuego;

    @GetMapping
    public String listarTodosLosJuegos(Model model){
        List<Juego> juegos = servicioJuego.findAll();
        model.addAttribute("juegos", juegos);
        return "index";
    }

    /*
    @PostMapping
    public void save(Juego juego){
        servicioJuego.save(juego);
    }

    @GetMapping("/{Id}")
    public Juego getById(@PathVariable("Id") Integer Id){
        return servicioJuego.findById(Id);
    }

    @DeleteMapping("/{Id}")
    public void deleteById(@PathVariable("Id") Integer Id){
        servicioJuego.delete(Id);
    }
    */

}