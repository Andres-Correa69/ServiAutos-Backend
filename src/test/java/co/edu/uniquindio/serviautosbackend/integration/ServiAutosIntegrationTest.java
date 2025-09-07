package co.edu.uniquindio.serviautosbackend.integration;

import co.edu.uniquindio.serviautosbackend.ServiAutosBackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Clase de pruebas de integración para ServiAutos Backend
 * 
 * Esta clase contiene pruebas de integración que verifican:
 * - Que el contexto de Spring Boot se carga correctamente
 * - Que todas las configuraciones y beans están disponibles
 * - Que las dependencias se inyectan correctamente
 * - Que la aplicación puede iniciar sin errores
 * 
 * Las pruebas de integración son diferentes a las pruebas unitarias:
 * - Prueban múltiples componentes trabajando juntos
 * - Verifican la configuración completa de la aplicación
 * - Aseguran que la integración entre capas funciona correctamente
 * 
 * Utiliza el perfil "test" para usar configuraciones específicas de pruebas
 */
@SpringBootTest(classes = ServiAutosBackendApplication.class)
@ActiveProfiles("test")
class ServiAutosIntegrationTest {

    /**
     * Test: Verificación de carga del contexto de Spring
     * 
     * Este test verifica que:
     * 1. La aplicación Spring Boot puede iniciar correctamente
     * 2. Todas las configuraciones (@Configuration) se cargan
     * 3. Todos los beans (@Component, @Service, @Repository) se crean
     * 4. Las dependencias se resuelven correctamente
     * 5. No hay errores de configuración o dependencias faltantes
     * 
     * Si este test falla, indica problemas en la configuración de la aplicación
     * como beans faltantes, configuraciones incorrectas, o dependencias no resueltas.
     */
    @Test
    void contextLoads() {
        // Este test verifica que el contexto de Spring se carga correctamente
        // con todas las configuraciones y beans necesarios
        // 
        // Si llegamos aquí sin excepciones, significa que:
        // - La clase principal ServiAutosBackendApplication se puede ejecutar
        // - Todas las anotaciones @Configuration se procesan correctamente
        // - Todos los @Component, @Service, @Repository se instancian
        // - Las dependencias se inyectan sin problemas
        // - La configuración de MongoDB, Security, JWT, etc. es válida
    }
}
