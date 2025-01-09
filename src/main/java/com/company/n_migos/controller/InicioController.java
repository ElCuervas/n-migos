package com.company.n_migos.controller;

import com.company.n_migos.dto.JuegoResponse;
import com.company.n_migos.dto.PaginaResponse;
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

        // Obtener la lista de juegos completa
        List<Juego> juegos = servicioJuego.findAll();

        System.out.println("Juegos encontrados: " + juegos.size());

        // Calcular la paginación
        PaginaResponse paginaResponse = calcularPaginacion(juegos, pagina, 18);

        System.out.println("Página actual: " + paginaResponse.getPaginaActual() + ", Total páginas: " + paginaResponse.getTotalPaginas());

        // Agregar los atributos al modelo
        model.addAttribute("juegos", paginaResponse.getJuegos());
        model.addAttribute("paginaActual", paginaResponse.getPaginaActual());
        model.addAttribute("totalPaginas", paginaResponse.getTotalPaginas());

        configurarEstadoUsuario(model, request);
        return "index";
    }


    // Método para calcular la paginación
    private PaginaResponse calcularPaginacion(List<Juego> juegos, int pagina, int juegosPorPagina) {
        int totalJuegos = juegos.size();
        int totalPaginas = (int) Math.ceil((double) totalJuegos / juegosPorPagina);

        int inicio = (pagina - 1) * juegosPorPagina;
        int fin = Math.min(inicio + juegosPorPagina, totalJuegos);
        List<Juego> juegosPagina = juegos.subList(inicio, fin); // Juegos para la página actual

        // Retornar la respuesta de la paginación
        return new PaginaResponse(
                juegosPagina.stream().map(JuegoResponse::new).collect(Collectors.toList()),
                totalPaginas,
                pagina
        );
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
    public ResponseEntity<?> buscarJuegos(@RequestParam("titulo") String titulo) {
        List<Juego> juegos = servicioJuego.buscarPorTitulo(titulo);
        return ResponseEntity.ok(
                juegos.stream()
                        .map(JuegoResponse::new)
                        .collect(Collectors.toList())
        );
    }


    @GetMapping("/generos")
    @ResponseBody
    public List<Genero> obtenerGeneros() {
        return generoServicio.findAll();
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public ResponseEntity<?> filtrarCombinado(
            @RequestParam(value = "generos", required = false) String generos,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "puntuacion", required = false) String puntuacionStr,
            @RequestParam(value = "pagina", defaultValue = "1") int pagina
    ) {
        System.out.println("Filtros aplicados: " + generos + ", " + tipo + ", " + puntuacionStr);
        Float puntuacion = null;
        try {
            puntuacion = puntuacionStr != null ? Float.parseFloat(puntuacionStr) : null;
        } catch (NumberFormatException e) {
            System.out.println("Puntuación inválida: " + puntuacionStr);
        }

        // Obtener los juegos filtrados
        List<String> listaGeneros = generos != null ? Arrays.asList(generos.split(",")) : null;
        List<Juego> juegos = servicioJuego.filtro(listaGeneros, tipo, puntuacion);

        // Calcular la paginación
        PaginaResponse paginaResponse = calcularPaginacion(juegos, pagina, 18);
        return ResponseEntity.ok().body(paginaResponse);
    }



    @GetMapping("/juegos")
    @ResponseBody
    public ResponseEntity<?> obtenerCatalogoCompleto(
            @RequestParam(value = "pagina", defaultValue = "1") int pagina
    ){
        final int juegosPorPagina = 18;
        List<Juego> juegos = servicioJuego.findAll();

        int totalJuegos = juegos.size();
        int totalPaginas = (int) Math.ceil((double) totalJuegos / juegosPorPagina);

        int inicio = (pagina - 1) * juegosPorPagina;
        int fin = Math.min(inicio + juegosPorPagina, totalJuegos);
        List<Juego> juegosPagina = juegos.subList(inicio, fin);

        return ResponseEntity.ok().body(new PaginaResponse(
                juegosPagina.stream().map(JuegoResponse::new).collect(Collectors.toList()),
                totalPaginas,
                pagina
        ));
    }




}