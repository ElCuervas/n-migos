package com.company.n_migos.controller;

import com.company.n_migos.dto.AuthResponse;
import com.company.n_migos.dto.RegisterRequest;
import com.company.n_migos.dto.ResenaResponse;
import com.company.n_migos.entity.User;
import com.company.n_migos.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ResenaController {
    private final ResenaService resenaService;
    private final JuegoServicio juegoService;
    private final UserService userService;
    @PostMapping("/resena-create")
    public ResponseEntity<String> CrearResena(HttpServletRequest request, @RequestBody ResenaResponse requestRes) {
        User user = userService.FindUser(request);
        try {
            Integer UserId= user.getId();
            resenaService.CrearResena(UserId,requestRes);
            juegoService.updateJuegoCalificacion(requestRes.getJuego());
            return ResponseEntity.ok("Reseña agregado");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya tienes una reseña agregado en este juego");
        }
    }
}
