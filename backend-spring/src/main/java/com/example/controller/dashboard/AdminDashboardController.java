package com.example.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/administrador")
public class AdminDashboardController {

    @GetMapping("")
    public String vistaPrincipal(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.admin) {
            return "redirect:/auth/login";
        }

        model.addAttribute("admin", u);
        return "admin/adminInicio";
    }


    @GetMapping("/inicio") public String inicio() { return "admin/adminInicio"; }
    @GetMapping("/usuarios") public String usuarios() { return "admin/adminUsuarios"; }
    @GetMapping("/psicologos") public String psicologos() { return "admin/adminPsicologos"; }
    @GetMapping("/reportes") public String reportes() { return "admin/adminReportes"; }
    @GetMapping("/contenido") public String contenido() { return "admin/adminContenido"; }
    @GetMapping("/terapias") public String terapias() { return "admin/adminTerapias"; }
    @GetMapping("/juegos") public String juegos() { return "admin/adminJuegos"; }
    @GetMapping("/configuracion") public String configuracion() { return "admin/adminConfiguracion"; }
}
