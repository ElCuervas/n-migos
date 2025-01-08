package com.company.n_migos.controller;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.service.JwtService;
import com.company.n_migos.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> addJuegoBiblioteca(@RequestHeader("Authorization") String token, @PathVariable Integer juegoId) {

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtService.getUsernameFromToken(jwt);
        User user = (User) userDetailsService.loadUserByUsername(username);

        userService.addJuegoBiblioteca(user, juegoId);

        return ResponseEntity.ok("Juego agregado a tu biblioteca");
    }
}
