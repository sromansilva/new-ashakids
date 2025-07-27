package com.example.config;

// import com.example.service.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // 🔒 Ruta protegida sin login debe redirigir a login
    @Test
    void accesoSinLoginRedirige() throws Exception {
        mockMvc.perform(get("/padre/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/login"));
    }

    // ✅ Login simulado con rol 'padre'
    @Test
    @WithMockUser(username = "padre1", authorities = { "padre" })
    void accesoConRolPadre() throws Exception {
        mockMvc.perform(get("/padre/home"))
                .andExpect(status().isOk());
    }

    // 🚫 Acceso de 'padre' a zona 'admin' debe fallar
    @Test
    @WithMockUser(username = "padre1", authorities = { "padre" })
    void accesoProhibidoARutaAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    // ✅ Ruta pública accesible sin login
    @Test
    void accesoRutaPublica() throws Exception {
        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk());
    }
}
