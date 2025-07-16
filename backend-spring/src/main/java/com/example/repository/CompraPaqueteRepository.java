package com.example.repository;

import com.example.model.CompraPaquete;
import com.example.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraPaqueteRepository extends JpaRepository<CompraPaquete, Integer> {
    List<CompraPaquete> findByPadreAndTerapeutaAndEstadoPago(Usuario padre, Usuario terapeuta, CompraPaquete.EstadoPago estadoPago);
    List<CompraPaquete> findByPadreAndEstadoPago(Usuario padre, CompraPaquete.EstadoPago estadoPago);
    List<CompraPaquete> findByPadre(Usuario padre);
    List<CompraPaquete> findByTerapeutaAndEstadoPago(Usuario terapeuta, CompraPaquete.EstadoPago estadoPago);
} 