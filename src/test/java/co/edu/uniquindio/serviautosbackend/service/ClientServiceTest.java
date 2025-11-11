package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.ClientCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ClientDTO;
import co.edu.uniquindio.serviautosbackend.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Client Service Integration Tests")
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Debería crear un cliente exitosamente")
    void shouldCreateClientSuccessfully() {
        // Given
        ClientCreationDTO dto = new ClientCreationDTO(
                "Juan",
                "Pérez",
                "1234567890",
                "juan.perez@example.com",
                "Calle 123",
                "3001234567"
        );

        // When
        ClientDTO created = clientService.create(dto);

        // Then
        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals("Juan", created.name());
        assertEquals("Pérez", created.lastName());
        assertEquals("juan.perez@example.com", created.email());
        assertEquals("1234567890", created.document());
    }

    @Test
    @DisplayName("Debería obtener un cliente por ID")
    void shouldGetClientById() {
        // Given
        ClientCreationDTO dto = new ClientCreationDTO(
                "María",
                "González",
                "0987654321",
                "maria.gonzalez@example.com",
                "Avenida 456",
                "3009876543"
        );
        ClientDTO created = clientService.create(dto);

        // When
        ClientDTO found = clientService.getById(created.id());

        // Then
        assertNotNull(found);
        assertEquals(created.id(), found.id());
        assertEquals("María", found.name());
        assertEquals("maria.gonzalez@example.com", found.email());
    }

    @Test
    @DisplayName("Debería obtener todos los clientes")
    void shouldGetAllClients() {
        // Given
        clientService.create(new ClientCreationDTO("Cliente1", "Apellido1", "111", "c1@test.com", "Dir1", "111"));
        clientService.create(new ClientCreationDTO("Cliente2", "Apellido2", "222", "c2@test.com", "Dir2", "222"));

        // When
        var clients = clientService.getAll();

        // Then
        assertNotNull(clients);
        assertTrue(clients.size() >= 2);
    }
}
