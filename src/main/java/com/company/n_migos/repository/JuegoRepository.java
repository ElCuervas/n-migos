package com.company.n_migos.repository;

import com.company.n_migos.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


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
}
