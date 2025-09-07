package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.domain.models.Client;
import co.edu.uniquindio.serviautosbackend.dto.ClientCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ClientDTO;
import co.edu.uniquindio.serviautosbackend.repository.ClientRepository;
import co.edu.uniquindio.serviautosbackend.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para ClientService
 * 
 * Esta clase prueba todas las operaciones CRUD del servicio de clientes:
 * - Crear cliente
 * - Obtener cliente por ID
 * - Obtener todos los clientes
 * - Actualizar cliente
 * - Eliminar cliente
 * 
 * Utiliza Mockito para simular el repositorio de clientes
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    // Mock del repositorio de clientes
    @Mock
    private ClientRepository clientRepository;

    // Inyección del servicio real con el mock
    @InjectMocks
    private ClientServiceImpl clientService;

    // Objetos de prueba reutilizables
    private Client testClient;
    private ClientCreationDTO clientCreationDTO;

    /**
     * Configuración inicial para cada test
     * Se ejecuta antes de cada método de prueba
     */
    @BeforeEach
    void setUp() {
        // Crear un cliente de prueba con datos válidos
        testClient = new Client();
        testClient.setId("1");
        testClient.setName("María");
        testClient.setLastName("García");
        testClient.setDocument("12345678");
        testClient.setEmail("maria@test.com");
        testClient.setAddress("Calle 456");
        testClient.setPhone("987654321");

        // Crear un DTO de creación de cliente de prueba
        clientCreationDTO = new ClientCreationDTO(
                "María",
                "García",
                "12345678",
                "maria@test.com",
                "Calle 456",
                "987654321"
        );
    }

    /**
     * Test: Crear cliente con datos válidos
     * 
     * Verifica que cuando se proporcionan datos válidos para crear un cliente:
     * 1. Se guarda el cliente en el repositorio
     * 2. Se retorna un ClientDTO con los datos correctos
     * 3. Se mapean correctamente todos los campos
     */
    @Test
    void create_ShouldReturnClientDTO_WhenValidData() {
        // Arrange: Configurar que el repositorio retorne el cliente guardado
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act: Crear el cliente
        ClientDTO result = clientService.create(clientCreationDTO);

        // Assert: Verificar que se retorna el DTO correcto
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("María", result.name());
        assertEquals("García", result.lastName());
        assertEquals("12345678", result.document());
        assertEquals("maria@test.com", result.email());
        assertEquals("Calle 456", result.address());
        assertEquals("987654321", result.phone());

        // Verificar que se llamó al método save del repositorio
        verify(clientRepository).save(any(Client.class));
    }

    /**
     * Test: Obtener cliente por ID cuando existe
     * 
     * Verifica que cuando se busca un cliente que existe:
     * 1. Se encuentra el cliente en el repositorio
     * 2. Se retorna un ClientDTO con los datos correctos
     */
    @Test
    void getById_ShouldReturnClientDTO_WhenClientExists() {
        // Arrange: Simular que el cliente existe en el repositorio
        when(clientRepository.findById("1")).thenReturn(Optional.of(testClient));

        // Act: Buscar el cliente por ID
        ClientDTO result = clientService.getById("1");

        // Assert: Verificar que se retorna el cliente correcto
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("María", result.name());
        assertEquals("maria@test.com", result.email());

        // Verificar que se llamó al método findById
        verify(clientRepository).findById("1");
    }

    /**
     * Test: Obtener cliente por ID cuando no existe
     * 
     * Verifica que cuando se busca un cliente que no existe:
     * 1. Se lanza una excepción con el mensaje correcto
     * 2. Se maneja correctamente el Optional vacío
     */
    @Test
    void getById_ShouldThrowException_WhenClientNotFound() {
        // Arrange: Simular que el cliente no existe
        when(clientRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert: Verificar que se lanza la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clientService.getById("999");
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clientRepository).findById("999");
    }

    /**
     * Test: Obtener todos los clientes cuando existen
     * 
     * Verifica que cuando existen clientes en el repositorio:
     * 1. Se retorna una lista con todos los clientes
     * 2. Se mapean correctamente todos los clientes a DTOs
     * 3. Se mantiene el orden de los clientes
     */
    @Test
    void getAll_ShouldReturnListOfClientDTOs_WhenClientsExist() {
        // Arrange: Crear un segundo cliente y simular que existen ambos
        Client client2 = new Client();
        client2.setId("2");
        client2.setName("Carlos");
        client2.setLastName("López");
        client2.setDocument("87654321");
        client2.setEmail("carlos@test.com");
        client2.setAddress("Calle 789");
        client2.setPhone("555555555");

        List<Client> clients = Arrays.asList(testClient, client2);
        when(clientRepository.findAll()).thenReturn(clients);

        // Act: Obtener todos los clientes
        List<ClientDTO> result = clientService.getAll();

        // Assert: Verificar que se retorna la lista correcta
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("María", result.get(0).name());
        assertEquals("Carlos", result.get(1).name());

        // Verificar que se llamó al método findAll
        verify(clientRepository).findAll();
    }

    /**
     * Test: Actualizar cliente cuando existe
     * 
     * Verifica que cuando se actualiza un cliente existente:
     * 1. Se encuentra el cliente por ID
     * 2. Se actualizan todos los campos con los nuevos datos
     * 3. Se guarda el cliente actualizado
     * 4. Se retorna el DTO con los datos actualizados
     */
    @Test
    void update_ShouldReturnUpdatedClientDTO_WhenClientExists() {
        // Arrange: Crear datos de actualización y simular que el cliente existe
        ClientCreationDTO updateDTO = new ClientCreationDTO(
                "María Actualizada",
                "García Actualizada",
                "12345678",
                "maria.actualizada@test.com",
                "Nueva Calle 456",
                "987654321"
        );

        when(clientRepository.findById("1")).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act: Actualizar el cliente
        ClientDTO result = clientService.update("1", updateDTO);

        // Assert: Verificar que se retorna el cliente actualizado
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("María Actualizada", result.name());
        assertEquals("García Actualizada", result.lastName());
        assertEquals("maria.actualizada@test.com", result.email());

        // Verificar que se llamaron los métodos correctos
        verify(clientRepository).findById("1");
        verify(clientRepository).save(testClient);
    }

    /**
     * Test: Actualizar cliente cuando no existe
     * 
     * Verifica que cuando se intenta actualizar un cliente inexistente:
     * 1. Se lanza una excepción con el mensaje correcto
     * 2. No se guarda nada en el repositorio
     */
    @Test
    void update_ShouldThrowException_WhenClientNotFound() {
        // Arrange: Simular que el cliente no existe
        when(clientRepository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert: Verificar que se lanza la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clientService.update("999", clientCreationDTO);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clientRepository).findById("999");
        verify(clientRepository, never()).save(any(Client.class));
    }

    /**
     * Test: Eliminar cliente cuando existe
     * 
     * Verifica que cuando se elimina un cliente:
     * 1. Se llama al método deleteById del repositorio
     * 2. No se lanzan excepciones
     */
    @Test
    void delete_ShouldDeleteClient_WhenClientExists() {
        // Arrange: Simular que la eliminación se ejecuta sin problemas
        doNothing().when(clientRepository).deleteById("1");

        // Act: Eliminar el cliente
        clientService.delete("1");

        // Assert: Verificar que se llamó al método deleteById
        verify(clientRepository).deleteById("1");
    }
}
