package com.example.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Usuario;
import com.example.model.Usuario.Rol;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/terapeuta")
public class TerapeutaDashboardController {

    @GetMapping("")
    public String mostrarVistaTerapeuta(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Rol.terapeuta) {
            return "redirect:/auth/login";
        }

        model.addAttribute("terapeuta", u);
        return "terapeuta/inicioTerapeuta";
    }

    @GetMapping("/inicio")
    public String inicio() { return "terapeuta/inicioTerapeuta"; }

    @GetMapping("/mensajes")
    public String mensajes() { return "terapeuta/mensajesTerapeuta"; }

    @GetMapping("/pacientes")
    public String pacientes() { return "terapeuta/pacientesTerapeuta"; }

    @GetMapping("/agenda")
    public String agenda() { return "terapeuta/agendaTerapeuta"; }

    @GetMapping("/sesiones")
    public String sesiones() { return "terapeuta/sesionesTerapeuta"; }

    @GetMapping("/material")
    public String material() { return "terapeuta/materialTerapeuta"; }

    @GetMapping("/progreso")
    public String progreso() { return "terapeuta/progresoTerapeuta"; }

    @GetMapping("/perfil")
    public String perfil() { return "terapeuta/perfilTerapeuta"; }

    @GetMapping("/configuracion")
    public String config() { return "terapeuta/configuracionTerapeuta"; }
}
