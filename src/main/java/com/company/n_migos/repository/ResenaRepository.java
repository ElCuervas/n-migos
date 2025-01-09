package com.company.n_migos.repository;

import com.company.n_migos.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    @Query("SELECT r FROM Resena r WHERE r.user.id = :userId AND r.juego.id = :juegoId")
    Optional<Resena> findByUserIdAndJuegoId(@Param("userId") Integer userId, @Param("juegoId") Integer juegoId);
    @Query(value = "SELECT * FROM resena WHERE juego_id = :juegoId", nativeQuery = true)
    List<Resena> findResenasByJuegoId(@Param("juegoId") Integer juegoId);
}
