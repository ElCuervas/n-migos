package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.service.JwtService;
import com.company.n_migos.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public String getPerfilPage(Model model,  HttpServletRequest request) {
        User user = userService.FindUser(request);
        List<Juego> juegos = userService.getBibliotecaByUsername(user.getUsername());
        if(!juegos.isEmpty()){
            model.addAttribute("juegos", juegos);
            model.addAttribute("notjuegos", true);
            System.out.println("hay juegos");
        } else{
            model.addAttribute("notjuegos", false);
            System.out.println("Sin juegos");
        }
        model.addAttribute("user",user);
        return "perfil";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEdicion(Model model, HttpServletRequest request) {
        String username = null;
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    username = jwtService.getUsernameFromToken(token);
                }
            }
        }

        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "editar-perfil";
    }

    @PostMapping("/editar")
    public String actualizarPerfil(User datosActualizados, HttpServletRequest request) {
        String username = null;
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    username = jwtService.getUsernameFromToken(token);
                }
            }
        }

        userService.updateUserProfile(username, datosActualizados);
        return "redirect:/perfil";
    }
}

