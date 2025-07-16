package com.example.controller.dashboard;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("")
    public String vistaPrincipal(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        // Obtener estadísticas reales
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        List<Usuario> terapeutas = usuarioRepository.findByRol(Usuario.Rol.terapeuta);
        List<Usuario> padres = usuarioRepository.findByRol(Usuario.Rol.padre);

        int usuariosTotal = todosUsuarios.size();
        int psicologosActivos = (int) terapeutas.stream()
            .filter(t -> t.getDisponible() != null && t.getDisponible())
            .count();
        int sesionesMensuales = 42; // si más adelante se conecta a la BD, reemplazar

        model.addAttribute("admin", u);
        model.addAttribute("usuariosTotal", usuariosTotal);
        model.addAttribute("psicologosActivos", psicologosActivos);
        model.addAttribute("sesionesMensuales", sesionesMensuales);

        return "admin/adminInicio";
    }

    @GetMapping("/panelDeControl")
    public String panelDeControl(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        model.addAttribute("admin", u);
        model.addAttribute("visitasTotales", 1247);
        model.addAttribute("usuariosActivos", 89);
        model.addAttribute("tiempoPromedio", "12 min");
        model.addAttribute("crecimiento", "+15%");

        return "admin/panelDeControl";
    }

    @GetMapping("/usuarios")
    public String usuarios(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        model.addAttribute("admin", u);
        model.addAttribute("usuarios", todosUsuarios);

        return "admin/adminUsuarios";
    }

    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        model.addAttribute("admin", u);
        model.addAttribute("totalUsuarios", 1247);
        model.addAttribute("sesionesCompletadas", 856);
        model.addAttribute("satisfaccionPromedio", 4.7);
        model.addAttribute("ingresosMensuales", "$45,230");
        model.addAttribute("fechaInicio", "2024-01-01");
        model.addAttribute("fechaFin", "2024-01-31");

        return "admin/adminReportes";
    }

    @GetMapping("/contenido")
    public String contenido(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        model.addAttribute("admin", u);
        return "admin/adminContenido";
    }

    @GetMapping("/terapias")
    public String terapias(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        model.addAttribute("admin", u);
        model.addAttribute("ninosEnTerapia", 89);
        model.addAttribute("comentariosRecibidos", 156);
        model.addAttribute("sesionesPendientes", 23);
        model.addAttribute("satisfaccionPromedio", 4.6);

        return "admin/adminTerapias";
    }

    @GetMapping("/juegos")
    public String juegos(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        model.addAttribute("admin", u);
        model.addAttribute("totalJuegos", 15);
        model.addAttribute("jugadoresActivos", 234);
        model.addAttribute("tiempoPromedio", "25 min");
        model.addAttribute("ratingPromedio", 4.5);

        return "admin/adminJuegos";
    }

}