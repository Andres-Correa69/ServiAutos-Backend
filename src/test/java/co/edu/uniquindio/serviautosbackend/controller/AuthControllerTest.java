package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.LoginDTO;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import co.edu.uniquindio.serviautosbackend.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@DisplayName("Auth Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.service.EmailService emailService;

    @MockBean
    private co.edu.uniquindio.serviautosbackend.service.VerificationCodeService verificationCodeService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("123");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setName("Test");
        testUser.setLastName("User");
        testUser.setRegisterDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debería hacer login exitosamente con credenciales válidas")
    void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO("test@example.com", "password123");
        String token = "test-jwt-token";

        when(authService.validateLogin(anyString(), anyString())).thenReturn(testUser);
        when(jwtService.generateToken(anyString())).thenReturn(token);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value(token));
    }

    @Test
    @DisplayName("Debería rechazar login con credenciales inválidas")
    void shouldRejectLoginWithInvalidCredentials() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO("test@example.com", "wrongPassword");

        when(authService.validateLogin(anyString(), anyString()))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    @DisplayName("Debería rechazar login con email inválido")
    void shouldRejectLoginWithInvalidEmail() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO("invalid-email", "password123");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debería rechazar login con body vacío")
    void shouldRejectLoginWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
