package com.example.controller.api;

import com.example.model.MaterialTerapeuta;
import com.example.model.Usuario;
import com.example.repository.MaterialTerapeutaRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/material")
public class MaterialTerapeutaController {

    @Autowired
    private MaterialTerapeutaRepository repo;

    // Ruta donde se guardarán los archivos (carpeta externa al proyecto)
    private final String RUTA_ARCHIVOS = "uploads/materiales/";

    // ✅ SUBIR ARCHIVO
    @PostMapping("/subir")
    public String subirArchivo(@RequestParam("titulo") String titulo,
                                @RequestParam("descripcion") String descripcion,
                                @RequestParam("archivo") MultipartFile archivo,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            redirectAttributes.addFlashAttribute("error", "Sesión no válida.");
            return "redirect:/terapeuta/material";
        }

        try {
            if (archivo.isEmpty() || !archivo.getContentType().equals("application/pdf")) {
                redirectAttributes.addFlashAttribute("error", "Solo se permiten archivos PDF.");
                return "redirect:/terapeuta/material";
            }

            // Generar nombre único
            String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path ruta = Paths.get(RUTA_ARCHIVOS + nombreArchivo);
            Files.createDirectories(ruta.getParent());
            Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

            // Crear y guardar objeto
            MaterialTerapeuta nuevo = new MaterialTerapeuta();
            nuevo.setTitulo(titulo);
            nuevo.setDescripcion(descripcion);
            nuevo.setNombreArchivo(nombreArchivo);
            nuevo.setUrlArchivo("/material/ver/" + nombreArchivo); // importante para poder acceder al PDF
            nuevo.setFechaSubida(LocalDate.now());
            nuevo.setTerapeuta(terapeuta);

            repo.save(nuevo);
            redirectAttributes.addFlashAttribute("success", "Archivo subido exitosamente.");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo.");
        }

        return "redirect:/terapeuta/material";
    }

    // ✅ VER ARCHIVO PDF
        @GetMapping("/ver/{nombre}")
        public void verArchivo(@PathVariable String nombre, HttpServletResponse response) throws IOException {
            Path archivo = Paths.get(RUTA_ARCHIVOS + nombre);

            if (Files.exists(archivo)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"" + nombre + "\"");
                Files.copy(archivo, response.getOutputStream());
                response.getOutputStream().flush();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }



    // ✅ MOSTRAR VISTA CON LOS MATERIALES (opcional si no lo haces desde otro controlador)
    @GetMapping("/vista")
    public String mostrarVistaMaterial(HttpSession session, Model model) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }

        List<MaterialTerapeuta> materiales = repo.findByTerapeuta(terapeuta);
        model.addAttribute("materiales", materiales);
        model.addAttribute("terapeuta", terapeuta);
        return "terapeuta/materialTerapeuta";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarMaterial(@PathVariable("id") Integer id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario terapeuta = (Usuario) session.getAttribute("usuarioObj");
        if (terapeuta == null || terapeuta.getRol() != Usuario.Rol.terapeuta) {
            return "redirect:/auth/login";
        }

        MaterialTerapeuta material = repo.findById(id).orElse(null);
        if (material != null && material.getTerapeuta().getId_usuario().equals(terapeuta.getId_usuario())) {
            try {
                // Eliminar archivo físico si existe
                Path archivoPath = Paths.get(RUTA_ARCHIVOS + material.getNombreArchivo());
                Files.deleteIfExists(archivoPath);

                // Eliminar de la base de datos
                repo.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Material eliminado correctamente.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error al eliminar el material.");
            }
        }

        return "redirect:/terapeuta/material";
    }

}
