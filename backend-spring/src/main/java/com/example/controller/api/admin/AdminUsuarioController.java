package com.example.controller.api.admin;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import com.example.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Listar usuarios con filtros (nombre/correo/codigo, rol, estado)
    @GetMapping("/lista")
    public ResponseEntity<List<Usuario>> listarUsuarios(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String estado) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        // Filtrar por búsqueda (nombre, correo, código)
        if (busqueda != null && !busqueda.isBlank()) {
            String b = busqueda.toLowerCase();
            usuarios = usuarios.stream().filter(u ->
                    (u.getNombre() != null && u.getNombre().toLowerCase().contains(b)) ||
                    (u.getCorreo() != null && u.getCorreo().toLowerCase().contains(b)) ||
                    (u.getCodigo() != null && u.getCodigo().toLowerCase().contains(b))
            ).collect(Collectors.toList());
        }
        // Filtrar por rol
        if (rol != null && !rol.isBlank()) {
            usuarios = usuarios.stream().filter(u ->
                    u.getRol() != null && u.getRol().name().equalsIgnoreCase(rol)
            ).collect(Collectors.toList());
        }
        // Filtrar por estado (activo/inactivo)
        if (estado != null && !estado.isBlank()) {
            if (estado.equalsIgnoreCase("activo")) {
                usuarios = usuarios.stream().filter(u -> u.getDisponible() == null || u.getDisponible()).collect(Collectors.toList());
            } else if (estado.equalsIgnoreCase("inactivo")) {
                usuarios = usuarios.stream().filter(u -> u.getDisponible() != null && !u.getDisponible()).collect(Collectors.toList());
            }
        }
        return ResponseEntity.ok(usuarios);
    }

    // 2. Agregar usuario
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarUsuario(@RequestBody Usuario usuario) {
        // Validaciones básicas
        if (usuario.getNombre() == null || usuario.getNombre().isBlank() ||
            usuario.getCorreo() == null || usuario.getCorreo().isBlank() ||
            usuario.getContrasena() == null || usuario.getContrasena().isBlank() ||
            usuario.getCodigo() == null || usuario.getCodigo().isBlank() ||
            usuario.getRol() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios");
        }
        // Validar unicidad de correo y código
        if (usuarioRepository.findByCodigo(usuario.getCodigo()).isPresent()) {
            return ResponseEntity.badRequest().body("El código ya está en uso");
        }
        if (usuarioRepository.findAll().stream().anyMatch(u -> u.getCorreo().equalsIgnoreCase(usuario.getCorreo()))) {
            return ResponseEntity.badRequest().body("El correo ya está en uso");
        }
        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        Usuario creado = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok(creado);
    }

    // 3. Editar usuario
    @PostMapping("/editar")
    public ResponseEntity<?> editarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId_usuario() == null) {
            return ResponseEntity.badRequest().body("ID de usuario requerido");
        }
        Optional<Usuario> existenteOpt = usuarioService.obtenerUsuarioPorId(usuario.getId_usuario());
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario existente = existenteOpt.get();
        // Solo actualizar campos permitidos
        existente.setNombre(usuario.getNombre());
        existente.setCorreo(usuario.getCorreo());
        existente.setCodigo(usuario.getCodigo());
        existente.setRol(usuario.getRol());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccion(usuario.getDireccion());
        existente.setEspecialidad(usuario.getEspecialidad());
        existente.setDisponible(usuario.getDisponible());
        existente.setNivel_acceso(usuario.getNivel_acceso());
        // No actualizar contraseña aquí
        Usuario actualizado = usuarioService.guardarUsuario(existente);
        return ResponseEntity.ok(actualizado);
    }

    // 4. Eliminar usuario
    @PostMapping("/eliminar")
    public ResponseEntity<?> eliminarUsuario(@RequestParam Integer id_usuario) {
        usuarioService.eliminarUsuario(id_usuario);
        return ResponseEntity.ok().build();
    }

    // 5. Cambiar contraseña
    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestParam Integer id_usuario, @RequestParam String nuevaContrasena) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id_usuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
            usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 