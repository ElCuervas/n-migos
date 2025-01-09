package com.company.n_migos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "desarrolladores", length = 200)
    private String desarrolladores;

    @ManyToMany(mappedBy = "biblioteca")
    private List<User> users;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resena> resenas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "generos_juego",
            joinColumns = @JoinColumn(name = "juego_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_nombre")
    )
    @JsonIgnore
    private List<Genero> generos;
}
