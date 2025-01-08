package com.company.n_migos.dto;

import com.company.n_migos.entity.Juego;
import lombok.Data;

import java.util.Date;

@Data
public class JuegoResponse {
    private Integer id;
    private String titulo;
    private Date lanzamiento;
    private String imagen;
    private Float calificacion;
    public JuegoResponse(Juego juego) {
        this.id = juego.getId();
        this.titulo = juego.getTitulo();
        this.lanzamiento = juego.getLanzamiento();
        this.imagen = juego.getImagen();
        this.calificacion = juego.getCalificacion();
    }

}
