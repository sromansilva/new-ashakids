package com.example.repository;

import com.example.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    // Métodos personalizados se pueden agregar aquí
    List<Mensaje> findByIdMensaje(Integer idMensaje);
    List<Mensaje> findByIdPadreAndIdTerapeutaOrderByFechaEnvioAsc(Integer idPadre, Integer idTerapeuta);
    List<Mensaje> findByIdPadreAndIdTerapeutaAndLeidoFalseAndEmisor(Integer idPadre, Integer idTerapeuta, Mensaje.Emisor emisor);
    List<Mensaje> findByIdPadreAndIdTerapeutaAndEliminadoFalseOrderByFechaEnvioAsc(Integer idPadre, Integer idTerapeuta);
} 