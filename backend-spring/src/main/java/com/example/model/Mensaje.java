package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@Setter
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Integer idMensaje;

    @Column(name = "id_padre", nullable = false)
    private Integer idPadre;

    @Column(name = "id_terapeuta", nullable = false)
    private Integer idTerapeuta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emisor emisor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(nullable = false)
    private Boolean leido = false;

    @Column(nullable = false)
    private Boolean eliminado = false;

    public enum Emisor {
        padre, terapeuta
    }
} 