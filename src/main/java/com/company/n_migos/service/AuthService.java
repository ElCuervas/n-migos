package com.company.n_migos.service;

import com.company.n_migos.dto.AuthResponse;
import com.company.n_migos.dto.LoginRequest;
import com.company.n_migos.dto.RegisterRequest;
import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.Role;
import com.company.n_migos.entity.User;
import com.company.n_migos.repository.JuegoRepository;
import com.company.n_migos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JuegoRepository juegoRepository;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode( request.getPassword()))
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .country(request.getCountry())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
        
    }

    public void addJuegoBiblioteca(User user, Integer juegoId) {
        Juego juegouser = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new IllegalArgumentException("Juego con ID " + juegoId + " no encontrado"));

        if (user.getBiblioteca().contains(juegouser)) {
            throw new IllegalArgumentException("El juego ya está en la biblioteca del usuario");
        }

        user.getBiblioteca().add(juegouser);

        userRepository.save(user);
    }

}
