package com.example.controller.api;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos los terapeutas
    @GetMapping("/terapeutas")
    public List<Map<String, Object>> listarTerapeutas() {
        return usuarioRepository.findByRol(Usuario.Rol.terapeuta).stream().map(t -> {
            java.util.HashMap<String, Object> map = new java.util.HashMap<>();
            map.put("id_usuario", t.getId_usuario());
            map.put("nombre", t.getNombre());
            map.put("especialidad", t.getEspecialidad());
            map.put("correo", t.getCorreo());
            map.put("fotoUrl", "/api/usuarios/foto/" + t.getId_usuario());
            map.put("descripcionCorta", t.getEspecialidad()); // Puedes cambiar esto por un campo real si lo tienes
            return map;
        }).collect(Collectors.toList());
    }

    // Detalles de un terapeuta
    @GetMapping("/terapeuta/{id}")
    public Map<String, Object> detalleTerapeuta(@PathVariable Integer id) {
        Usuario t = usuarioRepository.findById(id).orElseThrow();
        return Map.of(
            "id_usuario", t.getId_usuario(),
            "nombre", t.getNombre(),
            "especialidad", t.getEspecialidad(),
            "correo", t.getCorreo(),
            "fotoUrl", "/api/usuarios/foto/" + t.getId_usuario(),
            "descripcionLarga", t.getEspecialidad() // Puedes cambiar esto por un campo real si lo tienes
        );
    }

    // Endpoint para servir la foto del usuario
    @GetMapping("/foto/{id}")
    public @ResponseBody byte[] fotoUsuario(@PathVariable Integer id) {
        Usuario t = usuarioRepository.findById(id).orElse(null);
        if (t != null && t.getFoto() != null) {
            return t.getFoto();
        }
        return null;
    }
} 