package com.example.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ninos")
@Getter
@Setter
public class Nino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_nino;

    @NotBlank(message = "El nombre del niño es obligatorio.")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Debe estar asociado a un padre.")
    @Column(name = "id_padre")
    private Integer idPadre;

    // Campo para almacenar la foto del niño
    @Lob
    @Column(name = "foto")
    private byte[] foto;

    // Campo para el tipo MIME de la imagen
    @Column(name = "tipo_foto")
    private String tipoFoto = "image/jpeg";

    // Método para verificar si el niño tiene foto
    public boolean tieneFoto() {
        return foto != null && foto.length > 0;
    }
}
