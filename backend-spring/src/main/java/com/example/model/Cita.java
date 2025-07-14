package com.example.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Getter
@Setter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cita;

    @NotNull
    @Column(name = "id_nino")
    private Integer idNino;

    @ManyToOne
    @JoinColumn(name = "id_nino", insertable = false, updatable = false)
    private Nino nino;

    @NotNull
    @Column(name = "id_terapeuta")
    private Integer idTerapeuta;

    @ManyToOne
    @JoinColumn(name = "id_terapeuta", insertable = false, updatable = false)
    private Usuario terapeuta;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    @NotBlank
    @Size(max = 20)
    private String modalidad;

    @NotBlank
    @Size(max = 255)
    private String enlace_o_direccion;

    @Size(max = 20)
    private String estado = "programada";

    private String retroalimentacion_padre;

    @Column(name = "tema")
    private String tema;

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}
