package com.example.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Usuario;
import com.example.repository.CitaRepository;
import com.example.model.Cita;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/terapeuta")
public class TerapeutaDashboardController {

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping("")
    public String mostrarVistaTerapeuta(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }

        // Obtener solo las citas de este terapeuta
        List<Cita> citas = citaRepository.findByIdTerapeuta(u.getId_usuario());
        // Forzar carga de la relación Nino y otros campos relacionados
        for (Cita c : citas) {
            if (c.getNino() != null) {
                c.getNino().getNombre();
            }
            if (c.getTerapeuta() != null) {
                c.getTerapeuta().getNombre();
            }
        }
        model.addAttribute("citas", citas);
        model.addAttribute("terapeuta", u);
        return "terapeuta/inicioTerapeuta";
    }


    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }
        List<Cita> citas = citaRepository.findByIdTerapeuta(u.getId_usuario());
        // Forzar carga de la relación Nino
        for (Cita c : citas) {
            if (c.getNino() != null) {
                c.getNino().getNombre();
            }
        }
        model.addAttribute("citas", citas);
        model.addAttribute("terapeuta", u);
        return "terapeuta/inicioTerapeuta";
    }

    @GetMapping("/mensajes")
    public String mensajes() { return "terapeuta/mensajesTerapeuta"; }

    @GetMapping("/pacientes")
    public String pacientes() { return "terapeuta/pacientesTerapeuta"; }

    @GetMapping("/agenda")
    public String agenda(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }
        List<Cita> citas = citaRepository.findByIdTerapeuta(u.getId_usuario());
        model.addAttribute("citas", citas);
        model.addAttribute("terapeuta", u);
        return "terapeuta/agendaTerapeuta";
    }

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
