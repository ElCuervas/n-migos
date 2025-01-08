package com.company.n_migos.service;

import com.company.n_migos.entity.Genero;
import com.company.n_migos.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneroServicio {

    private final GeneroRepository generoRepository;

    public List<Genero> findAll() {
        return generoRepository.findAll();
    }

}
