package com.example.controller.dashboard;

import com.example.model.Cita;
import com.example.model.Usuario;
import com.example.repository.CitaRepository;
import com.example.repository.CompraPaqueteRepository;
import com.example.model.CompraPaquete;
import com.example.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/terapeuta")
public class TerapeutaDashboardController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private CompraPaqueteRepository compraPaqueteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    // ========== ENDPOINT FOTO PADRE ==========
    @GetMapping("/foto-padre/{idPadre}")
    @ResponseBody
    public ResponseEntity<byte[]> obtenerFotoPadre(@PathVariable Integer idPadre) {
        Usuario padre = usuarioRepository.findById(idPadre).orElse(null);
        if (padre == null || padre.getRol() != Usuario.Rol.padre || padre.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(padre.getTipoFoto()));
        headers.setContentLength(padre.getFoto().length);
        return new ResponseEntity<>(padre.getFoto(), headers, HttpStatus.OK);
    }

    // ========== OTRAS SECCIONES ==========
    @GetMapping("/mensajes")
    public String mensajes() {
        return "terapeuta/mensajesTerapeuta";
    }

    @GetMapping("/pacientes")
    public String pacientes(HttpSession session, Model model) {
        Usuario terapeuta = obtenerTerapeutaDesdeSesion(session);
        if (terapeuta == null) return "redirect:/auth/login";
        // Obtener compras pendientes de confirmación
        List<CompraPaquete> comprasPendientes = compraPaqueteRepository.findByTerapeutaAndEstadoPago(
            terapeuta, CompraPaquete.EstadoPago.pendiente);
        model.addAttribute("comprasPendientes", comprasPendientes);
        model.addAttribute("terapeuta", terapeuta);
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

    @GetMapping("/perfilTerapeuta")
    public String vistaPerfilTerapeuta(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }
        model.addAttribute("terapeuta", u);
        return "terapeuta/perfilTerapeuta";
    }

    @PostMapping("/subir-foto-perfil-terapeuta")
    public String subirFotoPerfilTerapeuta(@RequestParam("foto") org.springframework.web.multipart.MultipartFile foto, HttpSession session) {
        try {
            Usuario u = (Usuario) session.getAttribute("usuarioObj");
            if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
                return "redirect:/auth/login";
            }
            if (!foto.getContentType().startsWith("image/")) {
                return "redirect:/terapeuta/perfilTerapeuta?error=tipo_archivo_invalido";
            }
            if (foto.getSize() > 5 * 1024 * 1024) {
                return "redirect:/terapeuta/perfilTerapeuta?error=archivo_muy_grande";
            }
            u.setFoto(foto.getBytes());
            u.setTipoFoto(foto.getContentType());
            usuarioRepository.save(u);
            session.setAttribute("usuarioObj", u); // Actualizar en sesión
            return "redirect:/terapeuta/perfilTerapeuta?success=foto_guardada";
        } catch (Exception e) {
            return "redirect:/terapeuta/perfilTerapeuta?error=error_guardar";
        }
    }

    @GetMapping("/foto-perfil-terapeuta")
    public ResponseEntity<byte[]> obtenerFotoPerfilTerapeuta(HttpSession session, @RequestParam(value = "id", required = false) Integer id) {
        try {
            Usuario u;
            if (id != null) {
                u = usuarioRepository.findById(id).orElse(null);
                if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
                    return ResponseEntity.notFound().build();
                }
            } else {
                u = (Usuario) session.getAttribute("usuarioObj");
                if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
                    return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
                }
            }
            if (u.getFoto() == null || u.getFoto().length == 0) {
                return ResponseEntity.notFound().build();
            }
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.parseMediaType(u.getTipoFoto()));
            headers.setContentLength(u.getFoto().length);
            return new org.springframework.http.ResponseEntity<>(u.getFoto(), headers, org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cambiar-contrasena-terapeuta")
    public String cambiarContrasenaTerapeuta(HttpSession session,
            @RequestParam("actual") String actual,
            @RequestParam("nueva") String nueva,
            @RequestParam("confirmar") String confirmar) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }
        if (!nueva.equals(confirmar)) {
            return "redirect:/terapeuta/perfilTerapeuta?error=no_coinciden";
        }
        boolean esCorrecta = com.example.utils.GenerarHash.verificar(actual, u.getContrasena());
        if (!esCorrecta) {
            return "redirect:/terapeuta/perfilTerapeuta?error=actual_incorrecta";
        }
        u.setContrasena(com.example.utils.GenerarHash.encriptar(nueva));
        usuarioRepository.save(u);
        session.setAttribute("usuarioObj", u);
        return "redirect:/terapeuta/perfilTerapeuta?success=contrasena_cambiada";
    }

    @GetMapping("/configuracion")
    public String configuracion() {
        return "terapeuta/configuracionTerapeuta";
    }

    @GetMapping("/public/foto-terapeuta/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> obtenerFotoTerapeutaPublic(@PathVariable Integer id) {
        Usuario u = usuarioRepository.findById(id).orElse(null);
        if (u == null || u.getRol() != Usuario.Rol.terapeuta || u.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(u.getTipoFoto()));
        headers.setContentLength(u.getFoto().length);
        return new ResponseEntity<>(u.getFoto(), headers, HttpStatus.OK);
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
