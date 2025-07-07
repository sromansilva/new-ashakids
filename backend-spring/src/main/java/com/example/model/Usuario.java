package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Correo inválido.")
    @Size(max = 100)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 4)
    @Column(name = "contraseña")
    private String contrasena;

    @Size(max = 20)
    private String telefono;

    @NotBlank(message = "El código ASHA es obligatorio.")
    @Size(max = 20)
    @Column(unique = true)
    private String codigo;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    // Campos específicos por rol
    private String direccion;          // Solo para padres
    private String especialidad;       // Solo para terapeutas
    private Boolean disponible;        // Solo para terapeutas
    private String nivel_acceso;       // Solo para admins

    public enum Rol {
        padre, terapeuta, admin
    }
}
