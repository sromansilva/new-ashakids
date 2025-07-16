package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones_consumidas")
@Getter
@Setter
public class SesionConsumida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_sesion;

    @ManyToOne
    @JoinColumn(name = "id_compra")
    private CompraPaquete compra;

    @ManyToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;

    private Integer horas_consumidas;
    private LocalDateTime fecha;
} 