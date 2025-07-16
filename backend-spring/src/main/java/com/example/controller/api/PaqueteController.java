package com.example.controller.api;

import com.example.model.CompraPaquete;
import com.example.model.Paquete;
import com.example.model.Usuario;
import com.example.repository.PaqueteRepository;
import com.example.repository.UsuarioRepository;
import com.example.service.CompraPaqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/paquetes")
public class PaqueteController {
    @Autowired
    private PaqueteRepository paqueteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CompraPaqueteService compraPaqueteService;

    // Listar paquetes disponibles
    @GetMapping("")
    public List<Paquete> listarPaquetes() {
        return paqueteRepository.findAll();
    }

    // Comprar paquete (padre)
    @PostMapping("/comprar")
    public CompraPaquete comprarPaquete(@RequestParam Integer idPaquete, @RequestParam Integer idTerapeuta, HttpSession session) {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) throw new RuntimeException("No autorizado");
        Optional<Paquete> paqueteOpt = paqueteRepository.findById(idPaquete);
        Usuario terapeuta = usuarioRepository.findById(idTerapeuta).orElse(null);
        if (paqueteOpt.isEmpty() || terapeuta == null) throw new RuntimeException("Datos inválidos");
        return compraPaqueteService.comprarPaquete(padre, terapeuta, paqueteOpt.get());
    }

    // Consultar horas disponibles (padre)
    @GetMapping("/horas-disponibles")
    public int horasDisponibles(@RequestParam Integer idTerapeuta, HttpSession session) {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) throw new RuntimeException("No autorizado");
        Usuario terapeuta = usuarioRepository.findById(idTerapeuta).orElse(null);
        if (terapeuta == null) throw new RuntimeException("Terapeuta no encontrado");
        return compraPaqueteService.horasDisponibles(padre, terapeuta);
    }

    // Confirmar pago (terapeuta)
    @PostMapping("/confirmar-pago")
    public String confirmarPago(@RequestParam Integer idCompra, HttpSession session) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) throw new RuntimeException("No autorizado");
        Optional<CompraPaquete> compraOpt = compraPaqueteService.findById(idCompra);
        if (compraOpt.isEmpty()) throw new RuntimeException("Compra no encontrada");
        CompraPaquete compra = compraOpt.get();
        if (!compra.getTerapeuta().getId_usuario().equals(terapeuta.getId_usuario())) throw new RuntimeException("No autorizado para confirmar esta compra");
        compraPaqueteService.confirmarPago(compra);
        return "Pago confirmado";
    }

    // Consultar compras activas (padre)
    @GetMapping("/compras-activas")
    public List<CompraPaquete> comprasActivas(HttpSession session) {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) throw new RuntimeException("No autorizado");
        return compraPaqueteService.findByPadreAndEstadoPago(padre, CompraPaquete.EstadoPago.confirmado);
    }

    // Consultar compras pendientes (terapeuta)
    @GetMapping("/compras-pendientes")
    public List<CompraPaquete> comprasPendientes(HttpSession session) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) throw new RuntimeException("No autorizado");
        return compraPaqueteService.findByTerapeutaAndEstadoPago(terapeuta, CompraPaquete.EstadoPago.pendiente);
    }

    // Consultar compras pendientes (padre)
    @GetMapping("/compras-pendientes-padre")
    public List<CompraPaquete> comprasPendientesPadre(HttpSession session) {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) throw new RuntimeException("No autorizado");
        return compraPaqueteService.findByPadreAndEstadoPago(padre, CompraPaquete.EstadoPago.pendiente);
    }
} 