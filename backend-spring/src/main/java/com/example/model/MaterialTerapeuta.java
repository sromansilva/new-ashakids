package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "material_terapeuta")
@Getter
@Setter
public class MaterialTerapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material") // 👈 Esto es lo que faltaba
    private Long id;


    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String nombreArchivo;

    private String urlArchivo;

    private LocalDate fechaSubida;

    // Relación con el terapeuta que subió el archivo
    @ManyToOne
    @JoinColumn(name = "id_terapeuta")
    private Usuario terapeuta;

}
