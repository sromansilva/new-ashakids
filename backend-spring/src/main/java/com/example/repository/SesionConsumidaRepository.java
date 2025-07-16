package com.example.repository;

import com.example.model.SesionConsumida;
import com.example.model.CompraPaquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SesionConsumidaRepository extends JpaRepository<SesionConsumida, Integer> {
    List<SesionConsumida> findByCompra(CompraPaquete compra);
} 