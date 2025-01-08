package com.company.n_migos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genero")
public class Genero implements Serializable {

    @Id
    @Column(name = "nombre")
    private String nombre;

    @ManyToMany(mappedBy = "generos", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Juego> juegos;
}
