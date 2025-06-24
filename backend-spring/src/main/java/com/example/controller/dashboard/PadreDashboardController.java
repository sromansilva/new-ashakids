package com.example.controller.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Cita;
import com.example.model.Nino;
import com.example.model.Padre;
import com.example.model.Usuario;
import com.example.model.Usuario.Rol;
import com.example.repository.CitaRepository;
import com.example.repository.NinoRepository;
import com.example.repository.PadreRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/padre")
public class PadreDashboardController {

    @Autowired
    private PadreRepository padreRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping("")
    public String mostrarVistaPadre(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioObj");

        if (u == null || u.getRol() != Rol.padre) {
            return "redirect:/auth/login";
        }

        Padre padre = padreRepository.findById(u.getId_usuario()).orElse(null);
        if (padre != null) {
            List<Nino> ninos = ninoRepository.findByIdPadre(padre.getId_padre());
            List<Cita> citas = new ArrayList<>();
            for (Nino n : ninos) {
                citas.addAll(citaRepository.findByIdNino(n.getId_nino()));
            }
            model.addAttribute("citas", citas);
        }

        return "padre/padreInicio";
    
    }

    @GetMapping("/psicologos")
    public String vistaPsicologos(HttpSession session) {
        return "padre/psicologos";
    }

    @GetMapping("/agenda")
    public String vistaAgenda(HttpSession session) {
        return "padre/agenda";
    }

    @GetMapping("/recompensas")
    public String vistaRecompensas(HttpSession session) {
        return "padre/recompensas";
    }

    @GetMapping("/RinconDivertido")
    public String vistaRinconDivertido(HttpSession session) {
        return "padre/RinconDivertido";
    }


}
