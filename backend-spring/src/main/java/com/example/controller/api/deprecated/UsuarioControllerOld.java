/**
 * [OBSOLETO] Controlador REST para pruebas de usuarios.
 * Usado solo en fase de desarrollo con Postman.
 */

package com.example.controller.api.deprecated;

import com.example.model.Usuario;
import com.example.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControllerOld {

    @Autowired
    private UsuarioService usuarioService;

    // GET: Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Integer id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario creado = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok(creado);
    }

    // PUT: Actualizar usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @Valid @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuarioPorId(id);

        if (optionalUsuario.isPresent()) {
            Usuario existente = optionalUsuario.get();
            // Solo actualizamos los campos permitidos
            existente.setNombre(usuarioActualizado.getNombre());
            existente.setCorreo(usuarioActualizado.getCorreo());
            existente.setContraseña(usuarioActualizado.getContraseña());
            existente.setTelefono(usuarioActualizado.getTelefono());
            existente.setCodigo(usuarioActualizado.getCodigo());
            existente.setRol(usuarioActualizado.getRol());

            Usuario actualizado = usuarioService.guardarUsuario(existente);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (usuarioService.obtenerUsuarioPorId(id).isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
