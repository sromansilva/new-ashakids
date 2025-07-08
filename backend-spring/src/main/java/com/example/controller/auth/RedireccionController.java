package com.example.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RedireccionController {
    @GetMapping("/redireccionar")
    public String redirigirSegunRol(Authentication auth, HttpSession session) {
        Usuario u = usuarioRepository.findByCodigo(auth.getName()).orElse(null);
        if (u == null) return "redirect:/auth/login";

        session.setAttribute("usuarioObj", u);
        session.setAttribute("rol", u.getRol());
        session.setAttribute("codigo", u.getCodigo());
        session.setAttribute("usuario", u.getNombre());

        return switch (u.getRol()) {
            case admin -> "redirect:/admin";
            case padre -> "redirect:/padre";
            case terapeuta -> "redirect:/terapeuta";
        };
    }

    @Autowired
    private UsuarioRepository usuarioRepository;
}
