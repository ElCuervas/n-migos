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
}
