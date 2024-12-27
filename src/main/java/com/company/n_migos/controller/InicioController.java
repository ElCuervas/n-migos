package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.service.JuegoServicio;
import com.company.n_migos.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class InicioController {

    @Autowired
    private JuegoServicio servicioJuego;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public String listarTodosLosJuegos(Model model, HttpServletRequest request){
        List<Juego> juegos = servicioJuego.findAll();
        model.addAttribute("juegos", juegos);

        // Inicializar bandera para determinar si el usuario está autenticado
        boolean isLoggedIn = false;
        String username = null;
        String token = null;

        // Obtener el token de las cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) { // Supongamos que el nombre de la cookie es "token"
                    token = cookie.getValue();
                    try {
                        username = jwtService.getUsernameFromToken(token); // Extraer el nombre de usuario del JWT
                        isLoggedIn = true; // Usuario autenticado
                    } catch (Exception e) {
                        // Manejo de errores si el JWT es inválido o expirado
                        isLoggedIn = false;
                    }
                    break;
                }
            }
        }
        System.out.println(isLoggedIn);
        model.addAttribute("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            System.out.println(username);
            model.addAttribute("username", username); // Nombre de usuario si está autenticado
            model.addAttribute("token", token);
        }
        return "index";
    }

}