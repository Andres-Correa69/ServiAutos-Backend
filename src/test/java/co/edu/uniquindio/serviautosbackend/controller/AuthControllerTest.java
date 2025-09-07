package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.*;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import co.edu.uniquindio.serviautosbackend.service.EmailService;
import co.edu.uniquindio.serviautosbackend.service.JwtService;
import co.edu.uniquindio.serviautosbackend.service.VerificationCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para AuthController
 * 
 * Esta clase prueba todos los endpoints del controlador de autenticación:
 * - Login de usuarios
 * - Registro de nuevos usuarios (solicitud y verificación)
 * - Recuperación de contraseña (solicitud y reset)
 * 
 * Utiliza Mockito para simular todos los servicios dependientes:
 * - AuthService: Para operaciones de autenticación
 * - EmailService: Para envío de correos
 * - VerificationCodeService: Para códigos de verificación
 * - JwtService: Para generación de tokens JWT
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // Mock del servicio de autenticación
    @Mock
    private AuthService authService;

    // Mock del servicio de correo electrónico
    @Mock
    private EmailService emailService;

    // Mock del servicio de códigos de verificación
    @Mock
    private VerificationCodeService verificationService;

    // Mock del servicio JWT
    @Mock
    private JwtService jwtService;

    // Inyección del controlador real con todos los mocks
    @InjectMocks
    private AuthController authController;

    // Objeto de prueba reutilizable
    private User testUser;

    /**
     * Configuración inicial para cada test
     * Se ejecuta antes de cada método de prueba
     */
    @BeforeEach
    void setUp() {
        // Crear un usuario de prueba con datos válidos
        testUser = new User();
        testUser.setId("1");
        testUser.setName("Juan");
        testUser.setLastName("Pérez");
        testUser.setEmail("juan@test.com");
        testUser.setPassword("encodedPassword");
        testUser.setPhone("123456789");
        testUser.setAddress("Calle 123");
        testUser.setRegisterDate(LocalDateTime.now());
    }

    /**
     * Test: Login exitoso con credenciales válidas
     * 
     * Verifica que cuando se proporcionan credenciales válidas:
     * 1. Se valida el login correctamente
     * 2. Se genera un token JWT
     * 3. Se retorna una respuesta exitosa con el token
     * 4. Se incluye el mensaje de éxito
     */
    @Test
    void login_ShouldReturnSuccessResponse_WhenValidCredentials() throws Exception {
        // Arrange: Configurar el comportamiento esperado de los servicios
        LoginDTO loginDTO = new LoginDTO("juan@test.com", "password123");
        when(authService.validateLogin("juan@test.com", "password123")).thenReturn(testUser);
        when(jwtService.generateToken("juan@test.com")).thenReturn("jwt-token");

        // Act: Realizar el login
        ResponseEntity<ResponseDTO> response = authController.login(loginDTO);

        // Assert: Verificar que la respuesta es exitosa
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().error());
        assertEquals("Login successful", response.getBody().message());

        // Verificar que se llamaron los servicios correctos
        verify(authService).validateLogin("juan@test.com", "password123");
        verify(jwtService).generateToken("juan@test.com");
    }

    /**
     * Test: Login fallido con credenciales inválidas
     * 
     * Verifica que cuando se proporcionan credenciales inválidas:
     * 1. Se lanza una excepción en la validación
     * 2. Se retorna una respuesta de error 401 (Unauthorized)
     * 3. Se incluye el mensaje de error
     * 4. No se genera token JWT
     */
    @Test
    void login_ShouldReturnUnauthorizedResponse_WhenInvalidCredentials() throws Exception {
        // Arrange: Simular que las credenciales son inválidas
        LoginDTO loginDTO = new LoginDTO("juan@test.com", "wrongPassword");
        when(authService.validateLogin("juan@test.com", "wrongPassword"))
                .thenThrow(new Exception("Credenciales inválidas"));

        // Act: Intentar hacer login con credenciales incorrectas
        ResponseEntity<ResponseDTO> response = authController.login(loginDTO);

        // Assert: Verificar que la respuesta es de error
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().error());
        assertEquals("Credenciales inválidas", response.getBody().message());

        // Verificar que se llamó al servicio de validación pero no al de JWT
        verify(authService).validateLogin("juan@test.com", "wrongPassword");
        verify(jwtService, never()).generateToken(anyString());
    }

    /**
     * Test: Solicitud de registro exitosa con datos válidos
     * 
     * Verifica que cuando se solicita el registro de un nuevo usuario:
     * 1. Se verifica que el email no existe
     * 2. Se genera un código de verificación
     * 3. Se guarda el usuario pendiente
     * 4. Se envía un correo al administrador
     * 5. Se retorna una respuesta exitosa
     */
    @Test
    void requestSignup_ShouldReturnSuccessResponse_WhenValidData() throws Exception {
        // Arrange: Crear datos de usuario y configurar servicios
        UserCreationDTO userCreationDTO = new UserCreationDTO(
                "Juan",
                "Pérez",
                "123456789",
                "Calle 123",
                "juan@test.com",
                "password123",
                LocalDateTime.now()
        );

        when(authService.existsByEmail("juan@test.com")).thenReturn(false);
        when(verificationService.generateCode("juan@test.com", userCreationDTO)).thenReturn("123456");
        when(verificationService.getAdminEmail()).thenReturn("admin@serviautos.com");
        doNothing().when(verificationService).savePendingUser("juan@test.com", userCreationDTO);
        doNothing().when(emailService).sendEmail(any(EmailDTO.class));

        // Act: Solicitar registro de usuario
        ResponseEntity<ResponseDTO> response = authController.requestSignup(userCreationDTO);

        // Assert: Verificar que la respuesta es exitosa
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().error());
        assertEquals("Solicitud enviada al administrador", response.getBody().message());

        // Verificar que se ejecutaron todos los pasos del proceso
        verify(authService).existsByEmail("juan@test.com");
        verify(verificationService).generateCode("juan@test.com", userCreationDTO);
        verify(verificationService).savePendingUser("juan@test.com", userCreationDTO);
        verify(emailService).sendEmail(any(EmailDTO.class));
    }

    /**
     * Test: Solicitud de registro fallida cuando el email ya existe
     * 
     * Verifica que cuando se intenta registrar un email que ya existe:
     * 1. Se detecta que el email ya está registrado
     * 2. Se retorna una respuesta de error 400 (Bad Request)
     * 3. Se incluye el mensaje de error apropiado
     * 4. No se procesa el registro
     */
    @Test
    void requestSignup_ShouldReturnBadRequestResponse_WhenEmailAlreadyExists() throws Exception {
        // Arrange: Simular que el email ya existe
        UserCreationDTO userCreationDTO = new UserCreationDTO(
                "Juan",
                "Pérez",
                "123456789",
                "Calle 123",
                "juan@test.com",
                "password123",
                LocalDateTime.now()
        );

        when(authService.existsByEmail("juan@test.com")).thenReturn(true);

        // Act: Intentar registrar un email existente
        ResponseEntity<ResponseDTO> response = authController.requestSignup(userCreationDTO);

        // Assert: Verificar que la respuesta es de error
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().error());
        assertEquals("Email ya registrado", response.getBody().message());

        // Verificar que solo se verificó la existencia del email
        verify(authService).existsByEmail("juan@test.com");
        verify(verificationService, never()).generateCode(anyString(), any(UserCreationDTO.class));
        verify(emailService, never()).sendEmail(any(EmailDTO.class));
    }

    /**
     * Test: Verificación de registro exitosa con código válido
     * 
     * Verifica que cuando se verifica un registro con código válido:
     * 1. Se valida el código de verificación
     * 2. Se obtiene el usuario pendiente
     * 3. Se crea el usuario en el sistema
     * 4. Se elimina el usuario pendiente
     * 5. Se retorna una respuesta de éxito 201 (Created)
     */
    @Test
    void verifySignup_ShouldReturnSuccessResponse_WhenValidCode() throws Exception {
        // Arrange: Configurar datos de usuario pendiente y servicios
        UserCreationDTO pendingUser = new UserCreationDTO(
                "Juan",
                "Pérez",
                "123456789",
                "Calle 123",
                "juan@test.com",
                "password123",
                LocalDateTime.now()
        );

        when(verificationService.validateCode("juan@test.com", "123456")).thenReturn(true);
        when(verificationService.getPendingUser("juan@test.com")).thenReturn(pendingUser);
        when(authService.createUser(pendingUser)).thenReturn(testUser);
        doNothing().when(verificationService).removePendingUser("juan@test.com");

        // Act: Verificar el registro con código válido
        ResponseEntity<ResponseDTO> response = authController.verifySignup("juan@test.com", "123456");

        // Assert: Verificar que la respuesta es exitosa
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().error());
        assertEquals("Usuario creado exitosamente", response.getBody().message());

        // Verificar que se ejecutaron todos los pasos del proceso
        verify(verificationService).validateCode("juan@test.com", "123456");
        verify(verificationService).getPendingUser("juan@test.com");
        verify(authService).createUser(pendingUser);
        verify(verificationService).removePendingUser("juan@test.com");
    }

    /**
     * Test: Verificación de registro fallida con código inválido
     * 
     * Verifica que cuando se verifica un registro con código inválido:
     * 1. Se detecta que el código es inválido o expirado
     * 2. Se retorna una respuesta de error 400 (Bad Request)
     * 3. Se incluye el mensaje de error apropiado
     * 4. No se procesa la creación del usuario
     */
    @Test
    void verifySignup_ShouldReturnBadRequestResponse_WhenInvalidCode() throws Exception {
        // Arrange: Simular que el código es inválido
        when(verificationService.validateCode("juan@test.com", "999999")).thenReturn(false);

        // Act: Intentar verificar con código inválido
        ResponseEntity<ResponseDTO> response = authController.verifySignup("juan@test.com", "999999");

        // Assert: Verificar que la respuesta es de error
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().error());
        assertEquals("Código inválido o expirado", response.getBody().message());

        // Verificar que solo se validó el código
        verify(verificationService).validateCode("juan@test.com", "999999");
        verify(verificationService, never()).getPendingUser(anyString());
        verify(authService, never()).createUser(any(UserCreationDTO.class));
    }

    /**
     * Test: Solicitud de recuperación de contraseña exitosa
     * 
     * Verifica que cuando se solicita recuperar contraseña de un email existente:
     * 1. Se verifica que el email existe
     * 2. Se genera un código de recuperación
     * 3. Se envía un correo con el código
     * 4. Se retorna una respuesta exitosa
     */
    @Test
    void forgotPassword_ShouldReturnSuccessResponse_WhenEmailExists() throws Exception {
        // Arrange: Simular que el email existe y configurar servicios
        when(authService.existsByEmail("juan@test.com")).thenReturn(true);
        when(verificationService.generatePasswordResetCode("juan@test.com")).thenReturn("123456");
        doNothing().when(emailService).sendEmail(any(EmailDTO.class));

        // Act: Solicitar recuperación de contraseña
        ResponseEntity<ResponseDTO> response = authController.forgotPassword("juan@test.com");

        // Assert: Verificar que la respuesta es exitosa
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().error());
        assertEquals("Código enviado al correo", response.getBody().message());

        // Verificar que se ejecutaron todos los pasos del proceso
        verify(authService).existsByEmail("juan@test.com");
        verify(verificationService).generatePasswordResetCode("juan@test.com");
        verify(emailService).sendEmail(any(EmailDTO.class));
    }

    /**
     * Test: Solicitud de recuperación de contraseña fallida cuando el email no existe
     * 
     * Verifica que cuando se solicita recuperar contraseña de un email inexistente:
     * 1. Se detecta que el email no existe
     * 2. Se retorna una respuesta de error 400 (Bad Request)
     * 3. Se incluye el mensaje de error apropiado
     * 4. No se procesa la recuperación
     */
    @Test
    void forgotPassword_ShouldReturnBadRequestResponse_WhenEmailDoesNotExist() throws Exception {
        // Arrange: Simular que el email no existe
        when(authService.existsByEmail("nonexistent@test.com")).thenReturn(false);

        // Act: Intentar recuperar contraseña de email inexistente
        ResponseEntity<ResponseDTO> response = authController.forgotPassword("nonexistent@test.com");

        // Assert: Verificar que la respuesta es de error
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().error());
        assertEquals("Email no encontrado", response.getBody().message());

        // Verificar que solo se verificó la existencia del email
        verify(authService).existsByEmail("nonexistent@test.com");
        verify(verificationService, never()).generatePasswordResetCode(anyString());
        verify(emailService, never()).sendEmail(any(EmailDTO.class));
    }

    /**
     * Test: Reset de contraseña exitoso con código válido
     * 
     * Verifica que cuando se resetea la contraseña con código válido:
     * 1. Se valida el código de recuperación
     * 2. Se actualiza la contraseña del usuario
     * 3. Se retorna una respuesta exitosa
     */
    @Test
    void resetPassword_ShouldReturnSuccessResponse_WhenValidCode() throws Exception {
        // Arrange: Crear datos de reset y configurar servicios
        PasswordResetDTO resetDTO = PasswordResetDTO.builder()
                .email("juan@test.com")
                .code("123456")
                .newPassword("newPassword123")
                .build();
        
        when(verificationService.validatePasswordResetCode("juan@test.com", "123456")).thenReturn(true);
        doNothing().when(authService).updatePassword("juan@test.com", "newPassword123");

        // Act: Resetear la contraseña con código válido
        ResponseEntity<ResponseDTO> response = authController.resetPassword(resetDTO);

        // Assert: Verificar que la respuesta es exitosa
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().error());
        assertEquals("Contraseña actualizada exitosamente", response.getBody().message());

        // Verificar que se ejecutaron todos los pasos del proceso
        verify(verificationService).validatePasswordResetCode("juan@test.com", "123456");
        verify(authService).updatePassword("juan@test.com", "newPassword123");
    }

    /**
     * Test: Reset de contraseña fallido con código inválido
     * 
     * Verifica que cuando se intenta resetear contraseña con código inválido:
     * 1. Se detecta que el código es inválido o expirado
     * 2. Se retorna una respuesta de error 400 (Bad Request)
     * 3. Se incluye el mensaje de error apropiado
     * 4. No se actualiza la contraseña
     */
    @Test
    void resetPassword_ShouldReturnBadRequestResponse_WhenInvalidCode() throws Exception {
        // Arrange: Crear datos de reset con código inválido
        PasswordResetDTO resetDTO = PasswordResetDTO.builder()
                .email("juan@test.com")
                .code("999999")
                .newPassword("newPassword123")
                .build();
        
        when(verificationService.validatePasswordResetCode("juan@test.com", "999999")).thenReturn(false);

        // Act: Intentar resetear contraseña con código inválido
        ResponseEntity<ResponseDTO> response = authController.resetPassword(resetDTO);

        // Assert: Verificar que la respuesta es de error
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().error());
        assertEquals("Código inválido o expirado", response.getBody().message());

        // Verificar que solo se validó el código
        verify(verificationService).validatePasswordResetCode("juan@test.com", "999999");
        verify(authService, never()).updatePassword(anyString(), anyString());
    }
}
