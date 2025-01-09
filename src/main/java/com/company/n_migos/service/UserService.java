package com.company.n_migos.service;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.repository.JuegoRepository;
import com.company.n_migos.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JuegoRepository juegoRepository;
    private final JwtService jwtService;

    public List<Juego> getBibliotecaByUsername(String username) {
        return userRepository.findBibliotecaByUsername(username);
    }
    public void addJuegoBiblioteca(User user, Integer juegoId) {
        Juego juegoUser = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new IllegalArgumentException("Juego con ID " + juegoId + " no encontrado"));

        if (user.getBiblioteca().contains(juegoUser)) {
            throw new IllegalArgumentException("El juego ya está en la biblioteca del usuario");
        }

        user.getBiblioteca().add(juegoUser);

        userRepository.save(user);
    }
    public User FindUser(HttpServletRequest request){
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
        return (User) userDetailsService.loadUserByUsername(username);
    }
    public String FindUserToken(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }
}
