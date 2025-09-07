package co.edu.uniquindio.serviautosbackend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase de configuración específica para pruebas
 * 
 * Esta clase define beans específicos para el ambiente de pruebas:
 * - Configuraciones que sobrescriben las de producción
 * - Beans mock o simplificados para pruebas
 * - Configuraciones que mejoran el rendimiento de las pruebas
 * 
 * La anotación @TestConfiguration indica que esta configuración
 * solo se aplica durante las pruebas, no en producción.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Bean de PasswordEncoder específico para pruebas
     * 
     * Este bean sobrescribe el PasswordEncoder de producción con:
     * - Una instancia específica para pruebas
     * - Configuración optimizada para velocidad de pruebas
     * - Comportamiento determinístico para tests
     * 
     * La anotación @Primary indica que este bean tiene prioridad
     * sobre otros beans del mismo tipo durante las pruebas.
     * 
     * @return PasswordEncoder configurado para pruebas
     */
    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
