package com.company.n_migos.controller;

import com.company.n_migos.dto.JuegoResponse;
import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.service.JuegoServicio;
import com.company.n_migos.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class InicioController {

    @Autowired
    private final JuegoServicio servicioJuego;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public String listarTodosLosJuegos(@RequestParam(name = "pagina", defaultValue = "1") String paginaStr, Model model, HttpServletRequest request) {
        int pagina;
        try {
            pagina = Integer.parseInt(paginaStr);
        } catch (NumberFormatException e) {
            pagina = 1; // en caso de error
        }
        configurarPaginacion(pagina, model);
        configurarEstadoUsuario(model, request);
        return "index";
    }

    //controla la paginacion de el catalogo
    private void configurarPaginacion(int pagina, Model model) {
        final int juegosPorPagina = 15;
        List<Juego> juegos = servicioJuego.findAll();

        // Calcular total de páginas
        int totalJuegos = juegos.size();
        int totalPaginas = (int) Math.ceil((double) totalJuegos / juegosPorPagina);

        // Calcular inicio y fin para sublista
        int inicio = (pagina - 1) * juegosPorPagina;
        int fin = Math.min(inicio + juegosPorPagina, totalJuegos);
        List<Juego> juegosPagina = juegos.subList(inicio, fin); // Filtra los juegos para la página actual

        // Agregar juegos y datos de paginación al modelo
        model.addAttribute("juegos", juegosPagina);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
    }

    //controla el estado del usuario para permitir accesos a otros servicios
    private void configurarEstadoUsuario(Model model, HttpServletRequest request) {
        boolean isLoggedIn = false;
        String username = null;
        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    try {
                        username = jwtService.getUsernameFromToken(token);
                        isLoggedIn = true;
                    } catch (Exception e) {
                        isLoggedIn = false;
                    }
                    break;
                }
            }
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            model.addAttribute("username", username);
            model.addAttribute("token", token);
        }
    }



    @GetMapping("/buscar")
    @ResponseBody
    public List<JuegoResponse> buscarJuegos(@RequestParam("titulo") String titulo) {
        System.out.println("Buscando juegos con el título: " + titulo);
        List<Juego> juegos;
        if (titulo != null && !titulo.isEmpty()) {
            juegos = servicioJuego.buscarPorTitulo(titulo);
        } else {
            juegos = servicioJuego.findAll();
        }
        return juegos.stream()
                .map(JuegoResponse::new)
                .collect(Collectors.toList());
    }


}