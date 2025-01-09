package com.company.n_migos.service;

import com.company.n_migos.dto.AuthResponse;
import com.company.n_migos.dto.ResenaResponse;
import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.Resena;
import com.company.n_migos.repository.JuegoRepository;
import com.company.n_migos.repository.ResenaRepository;
import com.company.n_migos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final UserRepository userRepository;
    private final JuegoRepository juegoRepository;

    public void CrearResena(Integer userID, ResenaResponse request){
        if (resenaRepository.findByUserIdAndJuegoId(userID, request.getJuego()).isPresent()) {
            throw new IllegalArgumentException("El usuario ya ha creado una reseña para este juego.");
        }
        Resena resena = Resena.builder()
                .user(userRepository.findById(userID)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")))
                .juego(juegoRepository.findById(request.getJuego())
                        .orElseThrow(() -> new RuntimeException("Juego no encontrado")))
                .texto(request.getTexto())
                .fechaCreacion(LocalDate.now())
                .puntuacion(request.getPuntuacion())
                .build();
        resenaRepository.save(resena);
    }
    public List<Resena> obtenerResenasPorJuego(Integer juegoId) {
        List<Resena> resenas = resenaRepository.findResenasByJuegoId(juegoId);
        return (resenas != null && !resenas.isEmpty()) ? resenas : null;
    }
}
