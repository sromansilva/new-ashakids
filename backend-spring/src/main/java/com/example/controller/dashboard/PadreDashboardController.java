package com.example.controller.dashboard;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.model.Cita;
import com.example.model.Nino;
import com.example.model.Usuario;
import com.example.model.CompraPaquete;
import com.example.repository.CitaRepository;
import com.example.repository.NinoRepository;
import com.example.repository.UsuarioRepository;
import com.example.repository.CompraPaqueteRepository;
import com.example.utils.GenerarHash;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/padre")
public class PadreDashboardController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CompraPaqueteRepository compraPaqueteRepository;

    @GetMapping("")
    public String mostrarVistaPadre(HttpSession session, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        // Obtener citas reales de la base de datos
        List<Cita> citas = citaRepository.findByPadreId(u.getId_usuario());
        // Ordenar citas por fecha y hora (más cercanas primero)
        citas.sort((a, b) -> {
            int comparacionFecha = a.getFecha().compareTo(b.getFecha());
            if (comparacionFecha != 0) {
                return comparacionFecha;
            }
            return a.getHora().compareTo(b.getHora());
        });
        model.addAttribute("citas", citas);
        model.addAttribute("usuario", u.getNombre());
        model.addAttribute("padre", u);

        return "padre/padreInicio";
    }

    // Método auxiliar para agregar el padre al modelo
    private void agregarPadreAlModelo(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u != null && u.getRol() == Usuario.Rol.padre) {
            model.addAttribute("padre", u);
        }
    }

    @GetMapping("/psicologos")
    public String vistaPsicologos(HttpSession session, Model model) {
        agregarPadreAlModelo(session, model);
        return "padre/psicologos";
    }

    @GetMapping("/agenda")
    public String vistaAgenda(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        // Obtener citas reales de la base de datos
        List<Cita> citas = citaRepository.findByPadreId(u.getId_usuario());

        // Ordenar citas por fecha y hora (más cercanas primero)
        citas.sort((a, b) -> {
            // Primero comparar por fecha
            int comparacionFecha = a.getFecha().compareTo(b.getFecha());
            if (comparacionFecha != 0) {
                return comparacionFecha;
            }
            // Si la fecha es igual, comparar por hora
            return a.getHora().compareTo(b.getHora());
        });

        // Reorganizar las citas para que aparezcan en el grid en el orden correcto:
        // [0] [1] <- Primera fila
        // [2] [3] <- Segunda fila
        // Donde [0] es la más próxima, [1] es la segunda más próxima, etc.
        List<Cita> citasReorganizadas = new ArrayList<>();
        for (int i = 0; i < citas.size(); i += 4) {
            // Agregar las citas en el orden del grid
            if (i < citas.size())
                citasReorganizadas.add(citas.get(i));
            if (i + 1 < citas.size())
                citasReorganizadas.add(citas.get(i + 1));
            if (i + 2 < citas.size())
                citasReorganizadas.add(citas.get(i + 2));
            if (i + 3 < citas.size())
                citasReorganizadas.add(citas.get(i + 3));
        }

        citas = citasReorganizadas;

        model.addAttribute("citas", citas);
        model.addAttribute("padre", u);

        return "padre/agenda";
    }

    @GetMapping("/recompensas")
    public String vistaRecompensas(HttpSession session, Model model) {
        agregarPadreAlModelo(session, model);
        return "padre/recompensas";
    }

    @GetMapping("/RinconDivertido")
    public String vistaRinconDivertido(HttpSession session, Model model) {
        agregarPadreAlModelo(session, model);
        return "padre/RinconDivertido";
    }

    @GetMapping("/cuentos")
    public String vistaCuentos(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        model.addAttribute("padre", u);
        return "padre/cuentos";
    }

    @GetMapping("/cuentos-clasicos")
    public String vistaCuentosClasicos(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        model.addAttribute("padre", u);
        return "padre/cuentosClasicos";
    }

    @GetMapping("/cuentos-inventados")
    public String vistaCuentosInventados(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        model.addAttribute("padre", u);
        return "padre/cuentosInventados";
    }

    @GetMapping("/canciones")
    public String vistaCanciones(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        model.addAttribute("padre", u);
        return "padre/canciones";
    }

    @GetMapping("/adivinanzas")
    public String vistaAdivinanzas(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }

        model.addAttribute("padre", u);
        return "padre/adivinanzas";
    }




    @GetMapping("/compras")
    public String vistaCompras(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }
        List<CompraPaquete> compras = compraPaqueteRepository.findByPadre(u);
        model.addAttribute("compras", compras);
        model.addAttribute("padre", u);
        return "padre/compras";
    }

    @GetMapping("/mensajes")
    public String mensajesPadre(Model model, HttpSession session) {
        agregarPadreAlModelo(session, model);
        return "padre/mensajesPadre";
    }

    // NUEVOS MÉTODOS PARA MANEJAR FOTOS DE NIÑOS

    @GetMapping("/perfilPadre")
    public String vistaPerfilPadre(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }
        List<Nino> ninos = ninoRepository.findByIdPadre(u.getId_usuario());

        model.addAttribute("ninos", ninos);
        model.addAttribute("padre", u);
        return "padre/perfilPadre";
    }

    @PostMapping("/subir-foto-nino/{idNino}")
    public String subirFotoNino(@PathVariable Integer idNino,
            @RequestParam("foto") MultipartFile foto,
            HttpSession session) {
        try {
            Usuario u = (Usuario) session.getAttribute("usuarioObj");

            if (u == null || u.getRol() != Usuario.Rol.padre) {
                return "redirect:/auth/login";
            }

            // Verificar que el niño pertenece al padre
            Nino nino = ninoRepository.findById(idNino).orElse(null);
            if (nino == null || !nino.getIdPadre().equals(u.getId_usuario())) {
                return "redirect:/padre/perfilPadre?error=niño_no_encontrado";
            }

            // Validar tipo de archivo
            if (!foto.getContentType().startsWith("image/")) {
                return "redirect:/padre/perfilPadre?error=tipo_archivo_invalido";
            }

            // Validar tamaño (máximo 5MB)
            if (foto.getSize() > 5 * 1024 * 1024) {
                return "redirect:/padre/perfilPadre?error=archivo_muy_grande";
            }

            // Guardar foto en la base de datos
            nino.setFoto(foto.getBytes());
            nino.setTipoFoto(foto.getContentType());
            ninoRepository.save(nino);

            return "redirect:/padre/perfilPadre?success=foto_guardada";

        } catch (Exception e) {
            return "redirect:/padre/perfilPadre?error=error_guardar";
        }
    }

    @GetMapping("/foto-nino/{idNino}")
    public ResponseEntity<byte[]> obtenerFotoNino(@PathVariable Integer idNino, HttpSession session) {
        try {
            Usuario u = (Usuario) session.getAttribute("usuarioObj");

            if (u == null || u.getRol() != Usuario.Rol.padre) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Nino nino = ninoRepository.findById(idNino).orElse(null);

            if (nino == null || !nino.getIdPadre().equals(u.getId_usuario())) {
                return ResponseEntity.notFound().build();
            }

            if (!nino.tieneFoto()) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(nino.getTipoFoto()));
            headers.setContentLength(nino.getFoto().length);

            return new ResponseEntity<>(nino.getFoto(), headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/subir-foto-perfil")
    public String subirFotoPerfil(@RequestParam("foto") MultipartFile foto, HttpSession session) {
        try {
            Usuario u = (Usuario) session.getAttribute("usuarioObj");
            if (u == null || u.getRol() != Usuario.Rol.padre) {
                return "redirect:/auth/login";
            }
            if (!foto.getContentType().startsWith("image/")) {
                return "redirect:/padre/perfilPadre?error=tipo_archivo_invalido";
            }
            if (foto.getSize() > 5 * 1024 * 1024) {
                return "redirect:/padre/perfilPadre?error=archivo_muy_grande";
            }
            u.setFoto(foto.getBytes());
            u.setTipoFoto(foto.getContentType());
            usuarioRepository.save(u);
            session.setAttribute("usuarioObj", u); // Actualizar en sesión
            return "redirect:/padre/perfilPadre?success=foto_guardada";
        } catch (Exception e) {
            return "redirect:/padre/perfilPadre?error=error_guardar";
        }
    }

    @GetMapping("/foto-perfil")
    public ResponseEntity<byte[]> obtenerFotoPerfil(HttpSession session) {
        try {
            Usuario u = (Usuario) session.getAttribute("usuarioObj");
            if (u == null || u.getRol() != Usuario.Rol.padre) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (!u.tieneFoto()) {
                return ResponseEntity.notFound().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(u.getTipoFoto()));
            headers.setContentLength(u.getFoto().length);
            return new ResponseEntity<>(u.getFoto(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasena(HttpSession session,
            @RequestParam("actual") String actual,
            @RequestParam("nueva") String nueva,
            @RequestParam("confirmar") String confirmar) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.padre) {
            System.out.println("Usuario no en sesión o no es padre");
            return "redirect:/auth/login";
        }
        // Validar que la nueva y la confirmación coincidan
        if (!nueva.equals(confirmar)) {
            System.out.println("Las contraseñas nuevas no coinciden");
            return "redirect:/padre/configuracion?error=no_coinciden";
        }
        // Validar la contraseña actual usando GenerarHash
        System.out.println("Hash guardado: " + u.getContrasena());
        boolean esCorrecta = GenerarHash.verificar(actual, u.getContrasena());
        System.out.println("¿Contraseña actual correcta? " + esCorrecta);
        if (!esCorrecta) {
            System.out.println("La contraseña actual es incorrecta");
            return "redirect:/padre/configuracion?error=actual_incorrecta";
        }
        // Cambiar la contraseña usando GenerarHash
        u.setContrasena(GenerarHash.encriptar(nueva));
        usuarioRepository.save(u);
        session.setAttribute("usuarioObj", u);
        System.out.println("Contraseña cambiada correctamente");
        return "redirect:/padre/configuracion?success=contrasena_cambiada";
    }

    @PostMapping("/agregar-nino")
    public String agregarNino(@RequestParam("nombre") String nombre,
            @RequestParam("fechaNacimiento") String fechaNacimiento,
            HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");
        if (u == null || u.getRol() != Usuario.Rol.padre) {
            return "redirect:/auth/login";
        }
        try {
            com.example.model.Nino nino = new com.example.model.Nino();
            nino.setNombre(nombre);
            nino.setFechaNacimiento(java.time.LocalDate.parse(fechaNacimiento));
            nino.setIdPadre(u.getId_usuario());
            ninoRepository.save(nino);
            return "redirect:/padre/perfilPadre?success=nino_agregado";
        } catch (Exception e) {
            return "redirect:/padre/perfilPadre?error=error_guardar";
        }
    }

}
