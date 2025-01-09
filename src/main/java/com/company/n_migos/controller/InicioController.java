package com.company.n_migos.controller;

import com.company.n_migos.dto.JuegoResponse;
import com.company.n_migos.entity.Genero;
import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.repository.GeneroRepository;
import com.company.n_migos.service.GeneroServicio;
import com.company.n_migos.service.JuegoServicio;
import com.company.n_migos.service.JwtService;
import com.company.n_migos.service.UserService;
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

import java.sql.ClientInfoStatus;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class InicioController {

    @Autowired
    private final JuegoServicio servicioJuego;
    private final GeneroServicio generoServicio;
    private final JwtService jwtService;
    private final UserService userService;

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

        model.addAttribute("juegos", juegosPagina);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
    }

    //controla el estado del usuario para permitir accesos a otros servicios
    private void configurarEstadoUsuario(Model model, HttpServletRequest request) {
        boolean isLoggedIn = false;
        String username = null;
        String token = userService.FindUserToken(request);
        if (token != null) {
            User user = userService.FindUser(request);
            username= user.getUsername();
            isLoggedIn=true;
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

    @GetMapping("/generos")
    @ResponseBody
    public List<Genero> obtenerGeneros() {
        return generoServicio.findAll();
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public List<JuegoResponse> filtrarJuegos(@RequestParam("generos") String generos) {
        List<String> listaGeneros = Arrays.asList(generos.split(","));
        List<Juego> juegos = servicioJuego.buscarPorGeneros(listaGeneros);
        return juegos.stream().map(JuegoResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/juegos")
    @ResponseBody
    public List<JuegoResponse> obtenerCatalogoCompleto(){
        System.out.println("obtendiendo catalogo");
        List<Juego> juegos = servicioJuego.findAll();
        return juegos.stream()
                .map(JuegoResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/filtrarPuntuacion")
    @ResponseBody
    public List<JuegoResponse> filtrarPorPuntuacion(
            @RequestParam("tipo") String tipo,
            @RequestParam("puntuacion") Float puntuacion
    ) {
        List<Juego> juegos;
        if ("mayor".equals(tipo)) {
            juegos = servicioJuego.buscarPuntuacionMayorQue(puntuacion);
        } else if ("menor".equals(tipo)) {
            juegos = servicioJuego.buscarPuntuacionMenorQue(puntuacion);
        } else {
            throw new IllegalArgumentException("Tipo de filtro no válido: " + tipo);
        }
        return juegos.stream().map(JuegoResponse::new).collect(Collectors.toList());
    }

    @GetMapping("/filtrarCombinado")
    @ResponseBody
    public List<JuegoResponse> filtrarCombinado(
            @RequestParam(value = "generos", required = false) String generos,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "puntuacion", required = false) Float puntuacion
    ) {
        System.out.println(puntuacion);
        List<String> listaGeneros = generos != null ? Arrays.asList(generos.split(",")) : null;
        List<Juego> juegos = servicioJuego.filtrarCombinado(listaGeneros, tipo, puntuacion);
        return juegos.stream().map(JuegoResponse::new).collect(Collectors.toList());
    }



}