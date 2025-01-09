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
        return juegoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Juego> buscarPorGeneros(List<String> generos) {
        return juegoRepository.findByGenerosNombreIn(generos, generos.size());
    }

    public List<Juego> buscarPuntuacionMayorQue(Float puntuacion) {
        return juegoRepository.findByCalificacionGreaterThan(puntuacion);
    }

    public List<Juego> buscarPuntuacionMenorQue(Float puntuacion) {
        return juegoRepository.findByCalificacionLessThan(puntuacion);
    }

    public List<Juego> filtrarCombinado(List<String> generos, String tipo, Float puntuacion) {

        System.out.println();
        System.out.println("Filtros seleccionados: " + generos + ", " + tipo + " " + puntuacion);

        // Verifica si la lista esta vacia
        boolean tieneGeneros = !generos.get(0).equals("");

        if (tieneGeneros && tipo != null && puntuacion != null) {
            System.out.println("Filtro conjunto"); // Filtro combinado por géneros y puntuación
            return juegoRepository.findByGenerosYPuntuacion(generos, generos.size(), tipo, puntuacion);
        } else if (tieneGeneros) {
            System.out.println("Filtro por géneros"); // Solo por géneros
            return buscarPorGeneros(generos);
        } else if (tipo != null && puntuacion != null) {
            System.out.println("Filtro por puntuación"); // Solo por puntuación
            if ("mayor".equals(tipo)) {
                return buscarPuntuacionMayorQue(puntuacion);
            } else {
                return buscarPuntuacionMenorQue(puntuacion);
            }
        } else {
            // Sin filtros
            System.out.println("Sin filtros");
            return findAll();
        }
    }




}
