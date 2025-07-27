package com.example.controller.auth;

// import com.example.model.Usuario;
// import com.example.repository.UsuarioRepository;
// import com.example.utils.GenerarHash;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.HttpSession;
// import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    // @Autowired
    // private UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }
}
/*
    @PostMapping("/login")
    public String login(@RequestParam String codigo,
                        @RequestParam String contrasena,
                        HttpSession session,
                        Model model) {

        Optional<Usuario> usuarioDB = usuarioRepository.findByCodigoAndContrasena(codigo, contrasena);

        if (usuarioDB.isPresent()) {
            Usuario u = usuarioDB.get();
            session.setAttribute("usuarioObj", u);
            session.setAttribute("rol", u.getRol());
            session.setAttribute("codigo", u.getCodigo());
            session.setAttribute("usuario", u.getNombre());

            return switch (u.getRol()) {
                case padre -> "redirect:/padre";
                case terapeuta -> "redirect:/terapeuta";
                case admin -> "redirect:/admin";
                default -> {
                    model.addAttribute("error", "Rol desconocido");
                    yield "auth/login";
                }
            };
        } else {
            model.addAttribute("error", "Código o contraseña incorrectos");
            return "auth/login";
        }
    }
*/
