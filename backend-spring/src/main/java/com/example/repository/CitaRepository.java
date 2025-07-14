package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByIdNino(Integer idNino);
    
    List<Cita> findByIdTerapeuta(Integer id_terapeuta);
    
    @Query("SELECT c FROM Cita c JOIN Nino n ON c.idNino = n.id_nino WHERE n.idPadre = :idPadre")
    List<Cita> findByPadreId(@Param("idPadre") Integer idPadre);

}