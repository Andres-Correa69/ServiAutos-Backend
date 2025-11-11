package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@DisplayName("Auth Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.service.AuthService authService;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.service.JwtService jwtService;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.service.EmailService emailService;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.service.VerificationCodeService verificationCodeService;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.security.JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("El endpoint de login debe estar disponible")
    void loginEndpointShouldBeAvailable() throws Exception {
        // Solo verifica que el endpoint responde (sin validar el contenido)
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest()); // Espera BadRequest porque el body está vacío
    }
}
