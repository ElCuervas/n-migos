package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.service.JwtService;
import com.company.n_migos.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/biblioteca")
@RequiredArgsConstructor
public class Biblioteca {
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @GetMapping()
    public String biblioteca(Model model, HttpServletRequest request){
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
        List<Juego> juegos = userService.getBibliotecaByUsername(username);
        if(!juegos.isEmpty()){
            model.addAttribute("juegos", juegos);
            model.addAttribute("notjuegos", true);
            System.out.println("hay juegos");
        } else{
            model.addAttribute("notjuegos", false);
            System.out.println("Sin juegos");
        }
        return "biblioteca";
    }
    @PostMapping("/{juegoId}")
    public ResponseEntity<?> addJuegoBiblioteca(HttpServletRequest request, @PathVariable Integer juegoId) {
        User user = userService.FindUser(request);
        try {
            userService.addJuegoBiblioteca(user, juegoId);
            return ResponseEntity.ok("El juego ha sido agregado a la biblioteca.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El juego ya está en la biblioteca del usuario.");
        }
    }
}
