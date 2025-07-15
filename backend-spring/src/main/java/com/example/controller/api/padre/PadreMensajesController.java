package com.example.controller.api.padre;

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
@RequestMapping("/api/padre/mensajes")
public class PadreMensajesController {

    @Autowired
    private MensajeRepository mensajeRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Listar terapeutas con los que hay conversación y mensajes no leídos
    @GetMapping("/terapeutas")
    public ResponseEntity<?> listarTerapeutasConConversacion(HttpSession session,
                                                        @RequestParam(required = false) String busqueda) {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        // Buscar todos los terapeutas del sistema
        List<Usuario> terapeutas = usuarioRepository.findAll();
        terapeutas.removeIf(u -> u.getRol() != Usuario.Rol.terapeuta);
        // Si hay búsqueda, filtrar
        if (busqueda != null && !busqueda.isBlank()) {
            String b = busqueda.toLowerCase();
            terapeutas.removeIf(terapeuta ->
                (terapeuta.getNombre() == null || !terapeuta.getNombre().toLowerCase().contains(b)) &&
                (terapeuta.getCorreo() == null || !terapeuta.getCorreo().toLowerCase().contains(b))
            );
        }
        // Buscar todos los mensajes donde el padre es participante
        List<Mensaje> mensajes = mensajeRepository.findAll();
        // Map para contar no leídos por terapeuta
        Map<Integer, Integer> noLeidosPorTerapeuta = new java.util.HashMap<>();
        for (Mensaje m : mensajes) {
            if (m.getIdPadre().equals(padre.getId_usuario()) && m.getEmisor() == Mensaje.Emisor.terapeuta && !Boolean.TRUE.equals(m.getLeido())) {
                noLeidosPorTerapeuta.put(m.getIdTerapeuta(), noLeidosPorTerapeuta.getOrDefault(m.getIdTerapeuta(), 0) + 1);
            }
        }
        // Construir resultado
        List<Map<String, Object>> resultado = new java.util.ArrayList<>();
        for (Usuario terapeuta : terapeutas) {
            Map<String, Object> terapeutaInfo = new java.util.HashMap<>();
            terapeutaInfo.put("idTerapeuta", terapeuta.getId_usuario());
            terapeutaInfo.put("nombre", terapeuta.getNombre());
            terapeutaInfo.put("correo", terapeuta.getCorreo());
            terapeutaInfo.put("foto", null); // Por ahora null
            terapeutaInfo.put("noLeidos", noLeidosPorTerapeuta.getOrDefault(terapeuta.getId_usuario(), 0));
            resultado.add(terapeutaInfo);
        }
        return ResponseEntity.ok(resultado);
    }

    // 2. Listar mensajes entre padre y terapeuta (paginado)
    @GetMapping("/conversacion")
    public ResponseEntity<?> listarMensajes(@RequestParam Integer idTerapeuta,
                                            @RequestParam(defaultValue = "0") int pagina,
                                            @RequestParam(defaultValue = "20") int size,
                                            HttpSession session) {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        // Buscar mensajes no eliminados entre padre y terapeuta
        List<Mensaje> mensajes = mensajeRepository.findByIdPadreAndIdTerapeutaAndEliminadoFalseOrderByFechaEnvioAsc(
                padre.getId_usuario(), idTerapeuta);
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
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        // Validar datos de entrada
        Object idTerapeutaObj = payload.get("idTerapeuta");
        Object mensajeObj = payload.get("mensaje");
        if (idTerapeutaObj == null || mensajeObj == null) {
            return ResponseEntity.badRequest().body("Faltan datos obligatorios");
        }
        Integer idTerapeuta;
        try {
            idTerapeuta = Integer.parseInt(idTerapeutaObj.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("idTerapeuta inválido");
        }
        String mensajeTexto = mensajeObj.toString().trim();
        if (mensajeTexto.isEmpty()) {
            return ResponseEntity.badRequest().body("El mensaje no puede estar vacío");
        }
        // Verificar que el terapeuta existe y es rol terapeuta
        Usuario terapeuta = usuarioRepository.findById(idTerapeuta).orElse(null);
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return ResponseEntity.badRequest().body("Terapeuta no encontrado");
        }
        // Crear y guardar el mensaje
        Mensaje nuevo = new Mensaje();
        nuevo.setIdPadre(padre.getId_usuario());
        nuevo.setIdTerapeuta(idTerapeuta);
        nuevo.setEmisor(Mensaje.Emisor.padre);
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
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        Object idTerapeutaObj = payload.get("idTerapeuta");
        if (idTerapeutaObj == null) {
            return ResponseEntity.badRequest().body("Falta idTerapeuta");
        }
        Integer idTerapeuta;
        try {
            idTerapeuta = Integer.parseInt(idTerapeutaObj.toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("idTerapeuta inválido");
        }
        // Verificar que el terapeuta existe y es rol terapeuta
        Usuario terapeuta = usuarioRepository.findById(idTerapeuta).orElse(null);
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return ResponseEntity.badRequest().body("Terapeuta no encontrado");
        }
        // Buscar mensajes recibidos del terapeuta, no leídos, no eliminados
        List<Mensaje> mensajes = mensajeRepository.findByIdPadreAndIdTerapeutaAndEliminadoFalseOrderByFechaEnvioAsc(
            padre.getId_usuario(), idTerapeuta
        );
        int marcados = 0;
        for (Mensaje m : mensajes) {
            if (m.getEmisor() == Mensaje.Emisor.terapeuta && !Boolean.TRUE.equals(m.getLeido())) {
                m.setLeido(true);
                mensajeRepository.save(m);
                marcados++;
            }
        }
        return ResponseEntity.ok(Map.of("marcados", marcados));
    }
} 