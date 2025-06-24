package com.example.controller.api;

import com.example.model.Cita;
import com.example.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping
    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cita obtenerCitaPorId(@PathVariable Integer id) {
        return citaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Cita crearCita(@RequestBody Cita cita) {
        return citaRepository.save(cita);
    }

    @PutMapping("/{id}")
    public Cita actualizarCita(@PathVariable Integer id, @RequestBody Cita nuevaCita) {
        
        return citaRepository.findById(id).map(cita -> {
            cita.setIdNino(nuevaCita.getIdNino());
            cita.setIdTerapeuta(nuevaCita.getIdTerapeuta());
            cita.setFecha(nuevaCita.getFecha());
            cita.setHora(nuevaCita.getHora());
            cita.setModalidad(nuevaCita.getModalidad());
            cita.setEnlace_o_direccion(nuevaCita.getEnlace_o_direccion());
            cita.setEstado(nuevaCita.getEstado());
            cita.setRetroalimentacion_padre(nuevaCita.getRetroalimentacion_padre());
            return citaRepository.save(cita);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void eliminarCita(@PathVariable Integer id) {
        citaRepository.deleteById(id);
    }
}