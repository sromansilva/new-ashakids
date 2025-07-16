package com.example.repository;

import com.example.model.MaterialTerapeuta;
import com.example.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialTerapeutaRepository extends JpaRepository<MaterialTerapeuta, Integer> {
    List<MaterialTerapeuta> findByTerapeuta(Usuario terapeuta);
}
