package com.company.n_migos.repository;

import com.company.n_migos.entity.Genero;
import com.company.n_migos.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneroRepository extends JpaRepository<Genero, String> {
    List<Genero> findAll();
}