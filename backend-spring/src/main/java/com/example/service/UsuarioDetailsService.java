package com.example.service;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String codigo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("Rol asignado a usuario: " + usuario.getRol().name());
        System.out.println("Cargando usuario: " + usuario.getCodigo());
        System.out.println("Cargando usuario jamon: " + codigo); // Esto debería verse en consola

        return new User(
            usuario.getCodigo(),
            usuario.getContrasena(),
            List.of(new SimpleGrantedAuthority(usuario.getRol().name()))
        );
    }

}


