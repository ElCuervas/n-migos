package com.company.n_migos.controller;

import com.company.n_migos.entity.User;
import com.company.n_migos.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @GetMapping
    public String getPerfilPage(Model model,  HttpServletRequest request) {
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
        model.addAttribute("user",user);
        return "perfil";
    }
}

