package com.company.n_migos.service;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.repository.JuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JuegoServicio {

    private final JuegoRepository juegoRepository;

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Optional<Juego> findById(Integer Id) {
        return juegoRepository.findById(Id);
    }

    public Juego saveJuego(Juego juego) {
        return juegoRepository.save(juego);
    }


    public void updateJuego(Integer Id, Juego juego) {
        Juego juegoBd = findById(Id).orElseThrow(() ->
                new IllegalArgumentException("Juego con id " + Id + " no encontrado"));
        juegoBd.setTitulo(juego.getTitulo());
        juegoBd.setCalificacion(juego.getCalificacion());
        juegoBd.setImagen(juego.getImagen());
        juegoBd.setSinapsis(juego.getSinapsis());
        juegoBd.setLanzamiento(juego.getLanzamiento());
        juegoRepository.save(juegoBd);
    }

    public void deleteJuegoById(Integer Id) {
        juegoRepository.deleteById(Id);
    }

    public List<Juego> buscarPorTitulo(String titulo) {
        System.out.println("Buscando en la base de datos juegos con el título: " + titulo);
        return juegoRepository.findByTituloContainingIgnoreCase(titulo);
    }
}
