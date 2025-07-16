package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras_paquetes")
@Getter
@Setter
public class CompraPaquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_compra;

    @ManyToOne
    @JoinColumn(name = "id_padre")
    private Usuario padre;

    @ManyToOne
    @JoinColumn(name = "id_terapeuta")
    private Usuario terapeuta;

    @ManyToOne
    @JoinColumn(name = "id_paquete")
    private Paquete paquete;

    private LocalDateTime fecha_compra;
    private Integer horas_restantes;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago")
    private EstadoPago estadoPago = EstadoPago.pendiente;
    private Double deuda;

    public enum EstadoPago {
        pendiente, confirmado
    }
} 