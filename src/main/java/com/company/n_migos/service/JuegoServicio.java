package com.company.n_migos.service;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.Resena;
import com.company.n_migos.entity.User;
import com.company.n_migos.repository.JuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JuegoServicio {

    private final JuegoRepository juegoRepository;
    private final ResenaService resenaService;

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Optional<Juego> findById(Integer Id) {
        return juegoRepository.findById(Id);
    }

    public Juego saveJuego(Juego juego) {
        return juegoRepository.save(juego);
    }


    public void updateJuegoCalificacion(Integer Id) {
        List<Resena> resenas = resenaService.obtenerResenasPorJuego(Id);
        float calificacion = 0;
        if (resenas != null && !resenas.isEmpty()) {
            float sumaCalificaciones = 0;
            for (Resena resena : resenas) {
                sumaCalificaciones += resena.getPuntuacion();
            }
            calificacion = sumaCalificaciones / resenas.size();
            calificacion = Math.round(calificacion * 10) / 10f;
        }
        Juego juegoBd = findById(Id).orElseThrow(() ->
                new IllegalArgumentException("Juego con id " + Id + " no encontrado"));
        juegoBd.setCalificacion(calificacion);
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

    public List<Juego> filtro(List<String> generos, String tipo, Float puntuacion) {

        System.out.println();
        System.out.println("Filtros seleccionados: " + generos + ", " + tipo + " " + puntuacion);

        // Verifica si la lista esta vacia (puede ser cambiado dependiendo los requerimientos de la solicitud)
        boolean tieneGeneros = generos!=null;

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
