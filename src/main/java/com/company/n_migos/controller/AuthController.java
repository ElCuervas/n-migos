package com.company.n_migos.controller;

import com.company.n_migos.entity.User;
import com.company.n_migos.service.JwtService;
import com.company.n_migos.dto.LoginRequest;
import com.company.n_migos.dto.RegisterRequest;
import com.company.n_migos.service.AuthService;
import com.company.n_migos.dto.AuthResponse;
import com.company.n_migos.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService UserService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        // Crear una cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", authResponse.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 días
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie clearJwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearJwtCookie.toString())
                .body("Sesión cerrada correctamente");
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtService.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtService.getToken(userDetails);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(15 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .build();
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/biblioteca/{juegoId}")
    public ResponseEntity<?> addJuegoBiblioteca(@RequestHeader("Authorization") String token, @PathVariable Integer juegoId) {

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtService.getUsernameFromToken(jwt);
        User user = (User) userDetailsService.loadUserByUsername(username);

        UserService.addJuegoBiblioteca(user, juegoId);

        return ResponseEntity.ok("Juego agregado a tu biblioteca");
    }
}
