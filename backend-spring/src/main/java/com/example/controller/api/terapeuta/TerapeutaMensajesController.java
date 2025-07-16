package com.example.controller.api.terapeuta;

import com.example.model.Mensaje;
import com.example.model.Usuario;
import com.example.repository.MensajeRepository;
import com.example.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/terapeuta/mensajes")
public class TerapeutaMensajesController {

    @Autowired
    private MensajeRepository mensajeRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Listar padres con los que hay conversación y mensajes no leídos
    @GetMapping("/padres")
    public ResponseEntity<?> listarPadresConConversacion(HttpSession session,
                                                        @RequestParam(required = false) String busqueda) {
        // Obtener terapeuta logueado desde la sesión
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        // Buscar todos los padres del sistema
        List<Usuario> padres = usuarioRepository.findAll();
        padres.removeIf(u -> u.getRol() != Usuario.Rol.padre);
        // Si hay búsqueda, filtrar
        if (busqueda != null && !busqueda.isBlank()) {
            String b = busqueda.toLowerCase();
            padres.removeIf(padre ->
                (padre.getNombre() == null || !padre.getNombre().toLowerCase().contains(b)) &&
                (padre.getCorreo() == null || !padre.getCorreo().toLowerCase().contains(b))
            );
        }
        // Buscar todos los mensajes donde el terapeuta es participante
        List<Mensaje> mensajes = mensajeRepository.findAll();
        // Map para contar no leídos por padre
        Map<Integer, Integer> noLeidosPorPadre = new java.util.HashMap<>();
        for (Mensaje m : mensajes) {
            if (m.getIdTerapeuta().equals(terapeuta.getId_usuario()) && m.getEmisor() == Mensaje.Emisor.padre && !Boolean.TRUE.equals(m.getLeido())) {
                noLeidosPorPadre.put(m.getIdPadre(), noLeidosPorPadre.getOrDefault(m.getIdPadre(), 0) + 1);
            }
        }
        // Construir resultado
        List<Map<String, Object>> resultado = new java.util.ArrayList<>();
        for (Usuario padre : padres) {
            Map<String, Object> padreInfo = new java.util.HashMap<>();
            padreInfo.put("idPadre", padre.getId_usuario());
            padreInfo.put("nombre", padre.getNombre());
            padreInfo.put("correo", padre.getCorreo());
            padreInfo.put("foto", (padre.tieneFoto() ? "/padre/foto-perfil/" + padre.getId_usuario() : null));
            padreInfo.put("noLeidos", noLeidosPorPadre.getOrDefault(padre.getId_usuario(), 0));
            resultado.add(padreInfo);
        }
        return ResponseEntity.ok(resultado);
    }

    // 2. Listar mensajes entre terapeuta y padre (paginado)
    @GetMapping("/conversacion")
    public ResponseEntity<?> listarMensajes(@RequestParam Integer idPadre,
                                            @RequestParam(defaultValue = "0") int pagina,
                                            @RequestParam(defaultValue = "20") int size,
                                            HttpSession session) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        // Buscar mensajes no eliminados entre terapeuta y padre
        List<Mensaje> mensajes = mensajeRepository.findByIdPadreAndIdTerapeutaAndEliminadoFalseOrderByFechaEnvioAsc(
                idPadre, terapeuta.getId_usuario());
        int totalMensajes = mensajes.size();
        int fromIndex = Math.max(0, Math.min(pagina * size, totalMensajes));
        int toIndex = Math.max(0, Math.min(fromIndex + size, totalMensajes));
        List<Mensaje> paginaMensajes = mensajes.subList(fromIndex, toIndex);
        int totalPaginas = (int) Math.ceil((double) totalMensajes / size);
        // Mapear a DTO simple para el frontend
        List<Map<String, Object>> mensajesDTO = new java.util.ArrayList<>();
        for (Mensaje m : paginaMensajes) {
            Map<String, Object> dto = new java.util.HashMap<>();
            dto.put("id", m.getIdMensaje());
            dto.put("emisor", m.getEmisor().name());
            dto.put("mensaje", m.getMensaje());
            dto.put("fechaEnvio", m.getFechaEnvio());
            dto.put("leido", m.getLeido());
            mensajesDTO.add(dto);
        }
        Map<String, Object> respuesta = new java.util.HashMap<>();
        respuesta.put("mensajes", mensajesDTO);
        respuesta.put("pagina", pagina);
        respuesta.put("totalPaginas", totalPaginas);
        respuesta.put("totalMensajes", totalMensajes);
        return ResponseEntity.ok(respuesta);
    }

    // 3. Enviar mensaje
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarMensaje(@RequestBody Map<String, Object> payload, HttpSession session) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        // Validar datos de entrada
        Object idPadreObj = payload.get("idPadre");
        Object mensajeObj = payload.get("mensaje");
        if (idPadreObj == null || mensajeObj == null) {
            return ResponseEntity.badRequest().body("Faltan datos obligatorios");
        }
        Integer idPadre;
        try {
            idPadre = Integer.parseInt(idPadreObj.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("idPadre inválido");
        }
        String mensajeTexto = mensajeObj.toString().trim();
        if (mensajeTexto.isEmpty()) {
            return ResponseEntity.badRequest().body("El mensaje no puede estar vacío");
        }
        // Verificar que el padre existe y es rol padre
        Usuario padre = usuarioRepository.findById(idPadre).orElse(null);
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            return ResponseEntity.badRequest().body("Padre no encontrado");
        }
        // Crear y guardar el mensaje
        Mensaje nuevo = new Mensaje();
        nuevo.setIdPadre(idPadre);
        nuevo.setIdTerapeuta(terapeuta.getId_usuario());
        nuevo.setEmisor(Mensaje.Emisor.terapeuta);
        nuevo.setMensaje(mensajeTexto);
        nuevo.setFechaEnvio(java.time.LocalDateTime.now());
        nuevo.setLeido(false);
        nuevo.setEliminado(false);
        mensajeRepository.save(nuevo);
        return ResponseEntity.ok(Map.of(
            "id", nuevo.getIdMensaje(),
            "emisor", nuevo.getEmisor().name(),
            "mensaje", nuevo.getMensaje(),
            "fechaEnvio", nuevo.getFechaEnvio(),
            "leido", nuevo.getLeido()
        ));
    }

    // 4. Marcar mensajes como leídos
    @PostMapping("/marcar-leido")
    public ResponseEntity<?> marcarMensajesLeidos(@RequestBody Map<String, Object> payload, HttpSession session) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        Object idPadreObj = payload.get("idPadre");
        if (idPadreObj == null) {
            return ResponseEntity.badRequest().body("Falta idPadre");
        }
        Integer idPadre;
        try {
            idPadre = Integer.parseInt(idPadreObj.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("idPadre inválido");
        }
        // Verificar que el padre existe y es rol padre
        Usuario padre = usuarioRepository.findById(idPadre).orElse(null);
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            return ResponseEntity.badRequest().body("Padre no encontrado");
        }
        // Buscar mensajes recibidos del padre, no leídos, no eliminados
        List<Mensaje> mensajes = mensajeRepository.findByIdPadreAndIdTerapeutaAndEliminadoFalseOrderByFechaEnvioAsc(
            idPadre, terapeuta.getId_usuario()
        );
        int marcados = 0;
        for (Mensaje m : mensajes) {
            if (m.getEmisor() == Mensaje.Emisor.padre && !Boolean.TRUE.equals(m.getLeido())) {
                m.setLeido(true);
                mensajeRepository.save(m);
                marcados++;
            }
        }
        return ResponseEntity.ok(Map.of("marcados", marcados));
    }
}
