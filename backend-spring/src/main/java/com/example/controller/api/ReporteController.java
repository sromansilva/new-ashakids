package com.example.controller.api;

import com.example.model.CompraPaquete;
import com.example.model.Usuario;
import com.example.repository.CompraPaqueteRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class ReporteController {
    @Autowired
    private CompraPaqueteRepository compraPaqueteRepository;

    @GetMapping("/reporte/factura")
    public void generarFacturaPorCompra(@RequestParam Integer idCompra, HttpSession session, HttpServletResponse response) throws IOException {
        Usuario padre = (Usuario) session.getAttribute("usuarioObj");
        if (padre == null || padre.getRol() != Usuario.Rol.padre) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
            return;
        }
        Optional<CompraPaquete> compraOpt = compraPaqueteRepository.findById(idCompra);
        if (compraOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Compra no encontrada");
            return;
        }
        CompraPaquete compra = compraOpt.get();
        if (!compra.getPadre().getId_usuario().equals(padre.getId_usuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado para esta compra");
            return;
        }
        if (compra.getEstadoPago() != CompraPaquete.EstadoPago.confirmado) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La compra no está confirmada");
            return;
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura_ashakids_" + idCompra + ".pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            // LOGO
            InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/img/logofactura.jpg");
            if (logoStream != null) {
                Image logo = Image.getInstance(logoStream.readAllBytes());
                logo.scaleAbsolute(80, 80);
                logo.setAlignment(Image.LEFT);
                document.add(logo);
            }
            // FIRMA DIGITAL
            InputStream firmaStream = getClass().getClassLoader().getResourceAsStream("static/img/firma.jpg");
            if (firmaStream != null) {
                Image firma = Image.getInstance(firmaStream.readAllBytes());
                firma.scaleAbsolute(100, 40);
                firma.setAlignment(Image.RIGHT);
                firma.setSpacingBefore(30);
                document.add(firma);
            }
            Paragraph firmante = new Paragraph("Susana Horia\nPsicóloga Infantil", new Font(Font.HELVETICA, 10, Font.ITALIC));
            firmante.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(firmante);
            // FUENTES
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.WHITE);
            Font seccionTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(63, 81, 181));
            Font textoNormal = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Font textoTabla = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font pie = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);
            // ENCABEZADO
            PdfPTable header = new PdfPTable(1);
            header.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(new Phrase("🧾 FACTURA DE PAQUETE DE TERAPIA - ASHAKIDS", tituloFont));
            cell.setBackgroundColor(new Color(63, 81, 181));
            cell.setPadding(12);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            header.addCell(cell);
            document.add(header);
            document.add(Chunk.NEWLINE);
            // DATOS GENERALES
            Paragraph datos = new Paragraph();
            datos.setFont(textoNormal);
            datos.add("Fecha de emisión: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n");
            datos.add("Factura N°: " + String.format("%06d", compra.getId_compra()) + "\n");
            datos.add("Padre/Madre: " + padre.getNombre() + "\n");
            datos.add("Correo: " + padre.getCorreo() + "\n");
            datos.add("Terapeuta: " + (compra.getTerapeuta() != null ? compra.getTerapeuta().getNombre() : "-") + "\n");
            datos.setSpacingAfter(15);
            document.add(datos);
            // TÍTULO DE LA SECCIÓN
            Paragraph detallesTitulo = new Paragraph("🗓 Detalles del Paquete", seccionTitulo);
            detallesTitulo.setSpacingAfter(10);
            document.add(detallesTitulo);
            // TABLA DE DETALLES
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setSpacingAfter(10f);
            tabla.setWidths(new int[]{4, 3, 2});
            // ENCABEZADOS
            String[] encabezados = {"Descripción", "Paquete", "Monto"};
            for (String encabezado : encabezados) {
                PdfPCell encabezadoCell = new PdfPCell(new Phrase(encabezado, textoTabla));
                encabezadoCell.setBackgroundColor(new Color(224, 224, 224));
                encabezadoCell.setPadding(8);
                encabezadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(encabezadoCell);
            }
            // FILA
            String desc = "Sesiones de terapia con " + (compra.getTerapeuta() != null ? compra.getTerapeuta().getNombre() : "-") + " (" + (compra.getPaquete() != null ? compra.getPaquete().getHoras() : "-") + " horas)";
            tabla.addCell(new Phrase(desc, textoTabla));
            tabla.addCell(new Phrase(compra.getPaquete() != null ? compra.getPaquete().getNombre() : "-", textoTabla));
            tabla.addCell(new Phrase("S/. " + (compra.getPaquete() != null ? compra.getPaquete().getPrecio() : "-"), textoTabla));
            // TOTAL
            PdfPCell vacio = new PdfPCell(new Phrase(""));
            vacio.setColspan(2);
            vacio.setBorder(Rectangle.NO_BORDER);
            tabla.addCell(vacio);
            PdfPCell totalCell = new PdfPCell(new Phrase("Total: S/. " + (compra.getPaquete() != null ? compra.getPaquete().getPrecio() : "-"), totalFont));
            totalCell.setBackgroundColor(new Color(255, 235, 205));
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setPadding(8);
            tabla.addCell(totalCell);
            document.add(tabla);
            // NOTA FINAL
            Paragraph nota = new Paragraph("Gracias por confiar en ASHAKids. Si tienes preguntas, contáctanos a contacto@ashakids.pe", pie);
            nota.setSpacingBefore(20);
            nota.setAlignment(Element.ALIGN_CENTER);
            document.add(nota);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando el PDF: " + e.getMessage());
        }
    }
}
