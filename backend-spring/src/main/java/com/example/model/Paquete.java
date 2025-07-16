package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paquetes")
@Getter
@Setter
public class Paquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_paquete;

    private String nombre;
    private Integer horas;
    private Double precio;
} 