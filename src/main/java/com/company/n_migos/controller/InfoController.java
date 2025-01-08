package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.Resena;
import com.company.n_migos.service.JuegoServicio;
import com.company.n_migos.service.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {

    private final JuegoServicio servicioJuego;
    private final ResenaService resenaService;

    @GetMapping
    public String mostrarInfo(@RequestParam("id") Integer id, Model model) {
        Juego juego = servicioJuego.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));;
        List<Resena> resenas= resenaService.obtenerResenasPorJuego(juego.getId());
        model.addAttribute("juego", juego);
        model.addAttribute("resenas", resenas);
        return "info";
    }
}
