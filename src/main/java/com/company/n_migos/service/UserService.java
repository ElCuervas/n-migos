package com.company.n_migos.service;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import com.company.n_migos.repository.JuegoRepository;
import com.company.n_migos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JuegoRepository juegoRepository;

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
}
