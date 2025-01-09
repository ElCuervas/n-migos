package com.company.n_migos.repository;

import com.company.n_migos.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT j FROM Juego j WHERE LOWER(j.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Juego> buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT DISTINCT j FROM Juego j JOIN j.generos g WHERE g.nombre IN :generos GROUP BY j HAVING COUNT(DISTINCT g.nombre) = :cantidad")
    List<Juego> findByGenerosNombreIn(@Param("generos") List<String> generos, @Param("cantidad") int cantidad);

    List<Juego> findByCalificacionGreaterThan(Float calificacion);

    List<Juego> findByCalificacionLessThan(Float calificacion);

    @Query("SELECT DISTINCT j FROM Juego j " +
            "LEFT JOIN j.generos g " +
            "WHERE (:generos IS NULL OR g.nombre IN :generos) " +
            "AND (:tipo IS NULL OR " +
            "(CASE WHEN :tipo = 'mayor' THEN j.calificacion > :puntuacion ELSE j.calificacion < :puntuacion END)) " +
            "GROUP BY j.id " +
            "HAVING COUNT(DISTINCT g.nombre) = :cantidad OR :generos IS NULL")
    List<Juego> findByGenerosYPuntuacion(
            @Param("generos") List<String> generos,
            @Param("cantidad") int cantidad,
            @Param("tipo") String tipo,
            @Param("puntuacion") Float puntuacion
    );
}
