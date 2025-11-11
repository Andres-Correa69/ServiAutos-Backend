package co.edu.uniquindio.serviautosbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JWT Service Tests")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    @DisplayName("Debería generar un token válido")
    void shouldGenerateValidToken() {
        // Given
        String email = "test@example.com";

        // When
        String token = jwtService.generateToken(email);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Debería extraer el email del token")
    void shouldExtractEmailFromToken() {
        // Given
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        // When
        String extractedEmail = jwtService.extractUsername(token);

        // Then
        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Debería validar un token válido")
    void shouldValidateValidToken() {
        // Given
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        // When
        boolean isValid = jwtService.isTokenValid(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Debería rechazar un token inválido")
    void shouldRejectInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtService.isTokenValid(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debería rechazar un token nulo")
    void shouldRejectNullToken() {
        // When
        boolean isValid = jwtService.isTokenValid(null);

        // Then
        assertFalse(isValid);
    }
}
