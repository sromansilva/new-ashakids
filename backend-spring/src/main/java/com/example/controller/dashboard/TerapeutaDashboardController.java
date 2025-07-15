package com.example.controller.dashboard;

import com.example.model.Cita;
import com.example.model.Usuario;
import com.example.repository.CitaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/terapeuta")
public class TerapeutaDashboardController {

    @Autowired
    private CitaRepository citaRepository;

    // ========== VISTA PRINCIPAL ==========
    @GetMapping("")
    public String mostrarVistaTerapeuta(HttpSession session, Model model) {
        Usuario terapeuta = obtenerTerapeutaDesdeSesion(session);
        if (terapeuta == null) return "redirect:/auth/login";

        List<Cita> citas = obtenerCitasOrdenadasPorFecha(terapeuta.getId_usuario());
        model.addAttribute("citas", citas);
        model.addAttribute("terapeuta", terapeuta);
        return "terapeuta/inicioTerapeuta";
    }

    // ========== INICIO ==========
    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {
        Usuario terapeuta = obtenerTerapeutaDesdeSesion(session);
        if (terapeuta == null) return "redirect:/auth/login";

        List<Cita> citas = citaRepository.findByIdTerapeuta(terapeuta.getId_usuario());
        // Forzar carga de nombre del niño
        citas.forEach(c -> {
            if (c.getNino() != null) c.getNino().getNombre();
        });

        model.addAttribute("citas", citas);
        model.addAttribute("terapeuta", terapeuta);
        return "terapeuta/inicioTerapeuta";
    }

    // ========== AGENDA ==========
    @GetMapping("/agenda")
    public String agenda(HttpSession session, Model model) {
        Usuario terapeuta = obtenerTerapeutaDesdeSesion(session);
        if (terapeuta == null) return "redirect:/auth/login";

        List<Cita> citas = obtenerCitasOrdenadasPorFecha(terapeuta.getId_usuario());
        model.addAttribute("citas", citas);
        model.addAttribute("terapeuta", terapeuta);
        return "terapeuta/agendaTerapeuta";
    }

    // ========== SESIONES ==========
    @GetMapping("/sesiones")
    public String sesiones(HttpSession session, Model model) {
        Usuario terapeuta = obtenerTerapeutaDesdeSesion(session);
        if (terapeuta == null) return "redirect:/auth/login";

        List<Cita> sesiones = citaRepository.findByIdTerapeuta(terapeuta.getId_usuario());
        model.addAttribute("sesiones", sesiones != null ? sesiones : new ArrayList<>());
        model.addAttribute("terapeuta", terapeuta);
        return "terapeuta/sesionesTerapeuta";
    }

    @PostMapping("/sesiones/editar/{id}")
    public String procesarEdicion(@PathVariable Integer id,
                                   @RequestParam("retroalimentacion_padre") String retroalimentacion,
                                   HttpSession session) {
        if (!esTerapeuta(session)) return "redirect:/auth/login";

        Optional<Cita> optCita = citaRepository.findById(id);
        if (optCita.isPresent()) {
            Cita cita = optCita.get();
            cita.setRetroalimentacion_padre(retroalimentacion);
            citaRepository.save(cita);
        }

        return "redirect:/terapeuta/sesiones";
    }

    @DeleteMapping("/sesiones/{id}")
    @ResponseBody
    public Map<String, String> eliminar(@PathVariable Integer id, HttpSession session) {
        if (!esTerapeuta(session)) {
            return Map.of("status", "error", "message", "No autorizado");
        }

        citaRepository.deleteById(id);
        return Map.of("status", "ok");
    }

    // ========== OTRAS SECCIONES ==========
    @GetMapping("/mensajes")
    public String mensajes() {
        return "terapeuta/mensajesTerapeuta";
    }

    @GetMapping("/pacientes")
    public String pacientes() {
        return "terapeuta/pacientesTerapeuta";
    }

    @GetMapping("/material")
    public String material() {
        return "terapeuta/materialTerapeuta";
    }

    @GetMapping("/progreso")
    public String progreso() {
        return "terapeuta/progresoTerapeuta";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "terapeuta/perfilTerapeuta";
    }

    @GetMapping("/configuracion")
    public String configuracion() {
        return "terapeuta/configuracionTerapeuta";
    }

    // ========== MÉTODOS AUXILIARES ==========
    private Usuario obtenerTerapeutaDesdeSesion(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
            return null;
        }
        return u;
    }

    private boolean esTerapeuta(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        return u != null && u.getRol() == Usuario.Rol.terapeuta;
    }

    private List<Cita> obtenerCitasOrdenadasPorFecha(Integer idTerapeuta) {
        List<Cita> citas = citaRepository.findByIdTerapeuta(idTerapeuta);
        citas.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
        return citas;
    }
}
