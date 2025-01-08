package com.company.n_migos.dto;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResenaResponse {
    Integer juego;
    String texto;
    float puntuacion;
}
