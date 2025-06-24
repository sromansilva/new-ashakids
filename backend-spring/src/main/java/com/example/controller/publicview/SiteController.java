package com.example.controller.publicview;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteController {

    @GetMapping("/public/index")
    public String index() {
        return "public/index";
    }

    @GetMapping("/actividades")
    public String actividades() {
        return "public/actividades";
    }

    @GetMapping("/faq")
    public String faq() {
        return "public/faq";
    }

    @GetMapping("/guiapadres")
    public String guiaPadres() {
        return "public/guiapadres";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "public/nosotros";
    }

    @GetMapping("/opiniones")
    public String opiniones() {
        return "public/opiniones";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "public/servicios";
    }
    
    @GetMapping("/")
    public String redirigirInicio() {
        return "public/index";
    }

}
