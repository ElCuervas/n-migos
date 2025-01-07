package com.company.n_migos.repository;

import com.company.n_migos.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    List<Juego> findAll();
    /*
    @Modifying
    @Transactional
    @Query("UPDATE Juego j SET j.nombre = :nombre WHERE j.id = :id") crear consulta
    Juego saveJuego(Juego juego);
    @Query
    void updateJuego(Integer Id, Juego juego);
    @Query
    void deleteJuegoById(Integer Id);
    */
    List<Juego> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT j FROM Juego j WHERE LOWER(j.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Juego> buscarPorTitulo(@Param("titulo") String titulo);
}
