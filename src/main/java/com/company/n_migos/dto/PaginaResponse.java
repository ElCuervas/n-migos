package com.company.n_migos.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginaResponse {
    private List<JuegoResponse> juegos;
    private int totalPaginas;
    private int paginaActual;

    public PaginaResponse(List<JuegoResponse> juegos, int totalPaginas, int paginaActual) {
        this.juegos = juegos;
        this.totalPaginas = totalPaginas;
        this.paginaActual = paginaActual;
    }
}
