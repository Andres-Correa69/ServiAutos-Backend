package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;
import co.edu.uniquindio.serviautosbackend.repository.AuthRepository;
import co.edu.uniquindio.serviautosbackend.repository.UserRepository;
import co.edu.uniquindio.serviautosbackend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para AuthService
 * 
 * Esta clase prueba todos los métodos del servicio de autenticación:
 * - Creación de usuarios
 * - Validación de login
 * - Verificación de existencia de email
 * - Actualización de contraseñas
 * 
 * Utiliza Mockito para simular las dependencias (repositorios y encoder de contraseñas)
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    // Mock del repositorio de autenticación
    @Mock
    private AuthRepository authRepository;

    // Mock del repositorio de usuarios
    @Mock
    private UserRepository userRepository;

    // Mock del encoder de contraseñas
    @Mock
    private PasswordEncoder passwordEncoder;

    // Inyección del servicio real con los mocks
    @InjectMocks
    private AuthServiceImpl authService;

    // Objetos de prueba reutilizables
    private User testUser;
    private UserCreationDTO userCreationDTO;

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

        // Crear un DTO de creación de usuario de prueba
        userCreationDTO = new UserCreationDTO(
                "Juan",
                "Pérez",
                "123456789",
                "Calle 123",
                "juan@test.com",
                "password123",
                LocalDateTime.now()
        );
    }

    /**
     * Test: Crear usuario con datos válidos
     * 
     * Verifica que cuando se proporcionan datos válidos para crear un usuario:
     * 1. Se codifica la contraseña correctamente
     * 2. Se guarda el usuario en el repositorio
     * 3. Se retorna el usuario creado con los datos correctos
     */
    @Test
    void createUser_ShouldReturnCreatedUser_WhenValidData() {
        // Arrange (Preparar): Configurar el comportamiento esperado de los mocks
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authRepository.save(any(User.class))).thenReturn(testUser);

        // Act (Actuar): Ejecutar el método que queremos probar
        User result = authService.createUser(userCreationDTO);

        // Assert (Verificar): Comprobar que el resultado es el esperado
        assertNotNull(result);
        assertEquals("Juan", result.getName());
        assertEquals("Pérez", result.getLastName());
        assertEquals("juan@test.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        
        // Verificar que se llamaron los métodos correctos
        verify(passwordEncoder).encode("password123");
        verify(authRepository).save(any(User.class));
    }

    /**
     * Test: Validar login con credenciales correctas
     * 
     * Verifica que cuando se proporcionan credenciales válidas:
     * 1. Se encuentra el usuario por email
     * 2. Se valida la contraseña correctamente
     * 3. Se retorna el usuario encontrado
     */
    @Test
    void validateLogin_ShouldReturnUser_WhenValidCredentials() throws Exception {
        // Arrange: Simular que el usuario existe y la contraseña es correcta
        when(authRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act: Intentar hacer login
        User result = authService.validateLogin("juan@test.com", "password123");

        // Assert: Verificar que se retorna el usuario correcto
        assertNotNull(result);
        assertEquals("juan@test.com", result.getEmail());
        verify(authRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    /**
     * Test: Validar login cuando el usuario no existe
     * 
     * Verifica que cuando se intenta hacer login con un email que no existe:
     * 1. Se lanza una excepción con el mensaje correcto
     * 2. No se procesa la contraseña
     */
    @Test
    void validateLogin_ShouldThrowException_WhenUserNotFound() {
        // Arrange: Simular que el usuario no existe
        when(authRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert: Verificar que se lanza la excepción esperada
        Exception exception = assertThrows(Exception.class, () -> {
            authService.validateLogin("nonexistent@test.com", "password123");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(authRepository).findByEmail("nonexistent@test.com");
    }

    /**
     * Test: Validar login con contraseña incorrecta
     * 
     * Verifica que cuando se proporciona una contraseña incorrecta:
     * 1. Se encuentra el usuario por email
     * 2. Se valida la contraseña y falla
     * 3. Se lanza una excepción con el mensaje correcto
     */
    @Test
    void validateLogin_ShouldThrowException_WhenInvalidPassword() {
        // Arrange: Simular que el usuario existe pero la contraseña es incorrecta
        when(authRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act & Assert: Verificar que se lanza la excepción esperada
        Exception exception = assertThrows(Exception.class, () -> {
            authService.validateLogin("juan@test.com", "wrongPassword");
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(authRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
    }

    /**
     * Test: Verificar existencia de email cuando el usuario existe
     * 
     * Verifica que cuando se consulta por un email que existe:
     * 1. Se retorna true
     * 2. Se llama al método correcto del repositorio
     */
    @Test
    void existsByEmail_ShouldReturnTrue_WhenUserExists() {
        // Arrange: Simular que el usuario existe
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(testUser));

        // Act: Verificar si el email existe
        boolean result = authService.existsByEmail("juan@test.com");

        // Assert: Verificar que retorna true
        assertTrue(result);
        verify(userRepository).findByEmail("juan@test.com");
    }

    /**
     * Test: Verificar existencia de email cuando el usuario no existe
     * 
     * Verifica que cuando se consulta por un email que no existe:
     * 1. Se retorna false
     * 2. Se llama al método correcto del repositorio
     */
    @Test
    void existsByEmail_ShouldReturnFalse_WhenUserDoesNotExist() {
        // Arrange: Simular que el usuario no existe
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act: Verificar si el email existe
        boolean result = authService.existsByEmail("nonexistent@test.com");

        // Assert: Verificar que retorna false
        assertFalse(result);
        verify(userRepository).findByEmail("nonexistent@test.com");
    }

    /**
     * Test: Actualizar contraseña cuando el usuario existe
     * 
     * Verifica que cuando se actualiza la contraseña de un usuario existente:
     * 1. Se encuentra el usuario por email
     * 2. Se codifica la nueva contraseña
     * 3. Se guarda el usuario actualizado
     */
    @Test
    void updatePassword_ShouldUpdatePassword_WhenUserExists() {
        // Arrange: Simular que el usuario existe y configurar el comportamiento esperado
        when(userRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act: Actualizar la contraseña
        authService.updatePassword("juan@test.com", "newPassword");

        // Assert: Verificar que se ejecutaron los pasos correctos
        verify(userRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(testUser);
        assertEquals("newEncodedPassword", testUser.getPassword());
    }

    /**
     * Test: Actualizar contraseña cuando el usuario no existe
     * 
     * Verifica que cuando se intenta actualizar la contraseña de un usuario inexistente:
     * 1. Se lanza una excepción con el mensaje correcto
     * 2. No se guarda nada en el repositorio
     */
    @Test
    void updatePassword_ShouldThrowException_WhenUserNotFound() {
        // Arrange: Simular que el usuario no existe
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert: Verificar que se lanza la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.updatePassword("nonexistent@test.com", "newPassword");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findByEmail("nonexistent@test.com");
        verify(userRepository, never()).save(any(User.class));
    }
}
