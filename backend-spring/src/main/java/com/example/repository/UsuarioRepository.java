package com.example.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCodigoAndContraseña(String codigo, String contraseña);
    Optional<Usuario> findByCodigo(String codigo);
    List<Usuario> findByRol(Usuario.Rol rol);
}
