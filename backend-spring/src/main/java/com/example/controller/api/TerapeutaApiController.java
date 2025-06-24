package com.example.controller.api;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Terapeuta;
import com.example.repository.TerapeutaRepository;

@RestController
@RequestMapping("/api/terapeutas")
public class TerapeutaApiController {

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Terapeuta> obtenerTerapeuta(@PathVariable Long id) {
        Optional<Terapeuta> terapeutaOpt = terapeutaRepository.findById(id);
        if (terapeutaOpt.isPresent()) {
            return ResponseEntity.ok(terapeutaOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

