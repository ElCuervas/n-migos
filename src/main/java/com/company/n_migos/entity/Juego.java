package com.company.n_migos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="juego")
public class Juego implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="titulo", unique = true, nullable = false)
    private String titulo;

    @Column(name="lanzamiento")
    private Date lanzamiento;

    @Column(name="imagen")
    private String imagen;

    @Column(name="sinapsis", length = 3000)
    private String sinapsis;

    @Column(name="calificacion")
    private Float calificacion;


}
