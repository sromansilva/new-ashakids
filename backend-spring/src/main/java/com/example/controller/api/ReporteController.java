package com.example.controller.api;

import com.lowagie.text.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

@RestController
public class ReporteController {

    @GetMapping("/generar-factura")
    public void generarFactura(HttpServletResponse response) throws IOException {
    try{
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura_ashakids.pdf");

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        //LOGO
        InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/img/logofactura.jpg");
        Image logo = Image.getInstance(logoStream.readAllBytes());

        logo.scaleAbsolute(80, 80); // Tamaño del logo
        logo.setAlignment(Image.LEFT);
        document.add(logo);

        //FIRMA DIGITAL
        InputStream firmaStream = getClass().getClassLoader().getResourceAsStream("static/img/firma.jpg");
        Image firma = Image.getInstance(firmaStream.readAllBytes());

        firma.scaleAbsolute(100, 40); // Tamaño de la firma
        firma.setAlignment(Image.RIGHT);
        firma.setSpacingBefore(30);
        document.add(firma);

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
        PdfPCell cell = new PdfPCell(new Phrase("🧾 FACTURA DE CONSULTA - ASHAKIDS", tituloFont));
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
        datos.add("Fecha de emisión: " + LocalDate.now() + "\n");
        datos.add("Factura N°: 000123-AK\n");
        datos.add("Paciente: Juan Pérez\n");
        datos.add("Padre/Madre: Javier Pérez\n");
        datos.add("Correo: javier.perez@gmail.com\n");
        datos.setSpacingAfter(15);
        document.add(datos);

        // TÍTULO DE LA SECCIÓN
        Paragraph detallesTitulo = new Paragraph("🗓 Detalles de la Consulta", seccionTitulo);
        detallesTitulo.setSpacingAfter(10);
        document.add(detallesTitulo);

        // TABLA DE DETALLES
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);
        tabla.setSpacingAfter(10f);
        tabla.setWidths(new int[]{4, 3, 2});

        // ENCABEZADOS
        String[] encabezados = {"Descripción", "Psicóloga", "Monto"};
        for (String encabezado : encabezados) {
            PdfPCell encabezadoCell = new PdfPCell(new Phrase(encabezado, textoTabla));
            encabezadoCell.setBackgroundColor(new Color(224, 224, 224));
            encabezadoCell.setPadding(8);
            encabezadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(encabezadoCell);
        }

        // FILA
        tabla.addCell(new Phrase("Sesión de Psicoterapia Infantil - Modalidad Virtual (Zoom)", textoTabla));
        tabla.addCell(new Phrase("Susana Horia", textoTabla));
        tabla.addCell(new Phrase("S/. 80.00", textoTabla));

        // TOTAL
        PdfPCell vacio = new PdfPCell(new Phrase(""));
        vacio.setColspan(2);
        vacio.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(vacio);

        PdfPCell totalCell = new PdfPCell(new Phrase("Total: S/. 80.00", totalFont));
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
    e.printStackTrace(); // Loguea el error
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando el PDF: " + e.getMessage());
    }
    }
}
