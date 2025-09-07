package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.ClientCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ClientDTO;
import co.edu.uniquindio.serviautosbackend.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas unitarias para ClientController
 * 
 * Esta clase prueba todos los endpoints del controlador de clientes:
 * - Crear cliente (POST /api/clients)
 * - Obtener cliente por ID (GET /api/clients/{id})
 * - Obtener todos los clientes (GET /api/clients)
 * - Actualizar cliente (PUT /api/clients/{id})
 * - Eliminar cliente (DELETE /api/clients/{id})
 * 
 * Incluye dos tipos de pruebas:
 * 1. Pruebas unitarias directas del controlador
 * 2. Pruebas de integración con MockMvc para simular peticiones HTTP
 * 
 * Utiliza Mockito para simular el ClientService
 */
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    // Mock del servicio de clientes
    @Mock
    private ClientService clientService;

    // Inyección del controlador real con el mock
    @InjectMocks
    private ClientController clientController;

    // Objetos para pruebas HTTP con MockMvc
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    // Objetos de prueba reutilizables
    private ClientCreationDTO clientCreationDTO;
    private ClientDTO clientDTO;

    /**
     * Configuración inicial para cada test
     * Se ejecuta antes de cada método de prueba
     */
    @BeforeEach
    void setUp() {
        // Configurar MockMvc para pruebas HTTP
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        objectMapper = new ObjectMapper();

        // Crear datos de creación de cliente de prueba
        clientCreationDTO = new ClientCreationDTO(
                "María",
                "García",
                "12345678",
                "maria@test.com",
                "Calle 456",
                "987654321"
        );

        // Crear DTO de cliente de prueba
        clientDTO = new ClientDTO(
                "1",
                "María",
                "García",
                "12345678",
                "maria@test.com",
                "Calle 456",
                "987654321"
        );
    }

    // ========== PRUEBAS UNITARIAS DIRECTAS DEL CONTROLADOR ==========

    /**
     * Test: Crear cliente exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se proporcionan datos válidos para crear un cliente:
     * 1. Se llama al servicio con los datos correctos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene el cliente creado
     * 4. Todos los campos del cliente son correctos
     */
    @Test
    void create_ShouldReturnClientDTO_WhenValidData() throws Exception {
        // Arrange: Configurar que el servicio retorne el cliente creado
        when(clientService.create(any(ClientCreationDTO.class))).thenReturn(clientDTO);

        // Act: Crear el cliente llamando directamente al controlador
        ResponseEntity<ClientDTO> response = clientController.create(clientCreationDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().id());
        assertEquals("María", response.getBody().name());
        assertEquals("maria@test.com", response.getBody().email());

        // Verificar que se llamó al servicio con los datos correctos
        verify(clientService).create(clientCreationDTO);
    }

    /**
     * Test: Obtener cliente por ID exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se busca un cliente existente:
     * 1. Se llama al servicio con el ID correcto
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene el cliente encontrado
     */
    @Test
    void getById_ShouldReturnClientDTO_WhenClientExists() throws Exception {
        // Arrange: Configurar que el servicio retorne el cliente
        when(clientService.getById("1")).thenReturn(clientDTO);

        // Act: Buscar el cliente por ID
        ResponseEntity<ClientDTO> response = clientController.getById("1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().id());
        assertEquals("María", response.getBody().name());

        // Verificar que se llamó al servicio con el ID correcto
        verify(clientService).getById("1");
    }

    /**
     * Test: Obtener todos los clientes exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se solicitan todos los clientes:
     * 1. Se llama al servicio para obtener la lista
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la lista de clientes
     * 4. La lista tiene el número correcto de elementos
     */
    @Test
    void getAll_ShouldReturnListOfClientDTOs_WhenClientsExist() throws Exception {
        // Arrange: Crear una lista de clientes de prueba
        ClientDTO client2 = new ClientDTO(
                "2",
                "Carlos",
                "López",
                "87654321",
                "carlos@test.com",
                "Calle 789",
                "555555555"
        );

        List<ClientDTO> clients = Arrays.asList(clientDTO, client2);
        when(clientService.getAll()).thenReturn(clients);

        // Act: Obtener todos los clientes
        ResponseEntity<List<ClientDTO>> response = clientController.getAll();

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("María", response.getBody().get(0).name());
        assertEquals("Carlos", response.getBody().get(1).name());

        // Verificar que se llamó al servicio
        verify(clientService).getAll();
    }

    /**
     * Test: Actualizar cliente exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se actualiza un cliente existente:
     * 1. Se llama al servicio con el ID y datos correctos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene el cliente actualizado
     * 4. Los campos actualizados son correctos
     */
    @Test
    void update_ShouldReturnUpdatedClientDTO_WhenClientExists() throws Exception {
        // Arrange: Crear datos de actualización y cliente actualizado
        ClientCreationDTO updateDTO = new ClientCreationDTO(
                "María Actualizada",
                "García Actualizada",
                "12345678",
                "maria.actualizada@test.com",
                "Nueva Calle 456",
                "987654321"
        );

        ClientDTO updatedClientDTO = new ClientDTO(
                "1",
                "María Actualizada",
                "García Actualizada",
                "12345678",
                "maria.actualizada@test.com",
                "Nueva Calle 456",
                "987654321"
        );

        when(clientService.update("1", updateDTO)).thenReturn(updatedClientDTO);

        // Act: Actualizar el cliente
        ResponseEntity<ClientDTO> response = clientController.update("1", updateDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().id());
        assertEquals("María Actualizada", response.getBody().name());
        assertEquals("maria.actualizada@test.com", response.getBody().email());

        // Verificar que se llamó al servicio con los parámetros correctos
        verify(clientService).update("1", updateDTO);
    }

    /**
     * Test: Eliminar cliente exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se elimina un cliente:
     * 1. Se llama al servicio con el ID correcto
     * 2. Se retorna una respuesta HTTP 204 (No Content)
     * 3. No hay contenido en el cuerpo de la respuesta
     */
    @Test
    void delete_ShouldReturnNoContent_WhenClientExists() throws Exception {
        // Arrange: Configurar que el servicio elimine sin problemas
        doNothing().when(clientService).delete("1");

        // Act: Eliminar el cliente
        ResponseEntity<Void> response = clientController.delete("1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verificar que se llamó al servicio con el ID correcto
        verify(clientService).delete("1");
    }

    // ========== PRUEBAS DE INTEGRACIÓN CON MOCKMVC ==========

    /**
     * Test: Crear cliente exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint POST /api/clients funciona correctamente:
     * 1. Se envía una petición HTTP POST con datos JSON válidos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos correctos del cliente
     * 4. Se verifica cada campo individualmente usando jsonPath
     */
    @Test
    void create_ShouldReturnClientDTO_WhenValidDataViaHttp() throws Exception {
        // Arrange: Configurar que el servicio retorne el cliente creado
        when(clientService.create(any(ClientCreationDTO.class))).thenReturn(clientDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("María"))
                .andExpect(jsonPath("$.email").value("maria@test.com"));

        // Verificar que se llamó al servicio
        verify(clientService).create(any(ClientCreationDTO.class));
    }

    /**
     * Test: Obtener cliente por ID exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/clients/{id} funciona correctamente:
     * 1. Se envía una petición HTTP GET con el ID del cliente
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos correctos del cliente
     */
    @Test
    void getById_ShouldReturnClientDTO_WhenClientExistsViaHttp() throws Exception {
        // Arrange: Configurar que el servicio retorne el cliente
        when(clientService.getById("1")).thenReturn(clientDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("María"))
                .andExpect(jsonPath("$.email").value("maria@test.com"));

        // Verificar que se llamó al servicio
        verify(clientService).getById("1");
    }

    /**
     * Test: Obtener todos los clientes exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/clients funciona correctamente:
     * 1. Se envía una petición HTTP GET sin parámetros
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta es un array con los clientes correctos
     * 4. Se verifica el tamaño del array y los datos de cada elemento
     */
    @Test
    void getAll_ShouldReturnListOfClientDTOs_WhenClientsExistViaHttp() throws Exception {
        // Arrange: Crear lista de clientes y configurar servicio
        ClientDTO client2 = new ClientDTO(
                "2",
                "Carlos",
                "López",
                "87654321",
                "carlos@test.com",
                "Calle 789",
                "555555555"
        );

        List<ClientDTO> clients = Arrays.asList(clientDTO, client2);
        when(clientService.getAll()).thenReturn(clients);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("María"))
                .andExpect(jsonPath("$[1].name").value("Carlos"));

        // Verificar que se llamó al servicio
        verify(clientService).getAll();
    }

    /**
     * Test: Actualizar cliente exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint PUT /api/clients/{id} funciona correctamente:
     * 1. Se envía una petición HTTP PUT con datos JSON válidos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos actualizados del cliente
     */
    @Test
    void update_ShouldReturnUpdatedClientDTO_WhenClientExistsViaHttp() throws Exception {
        // Arrange: Crear datos de actualización y cliente actualizado
        ClientCreationDTO updateDTO = new ClientCreationDTO(
                "María Actualizada",
                "García Actualizada",
                "12345678",
                "maria.actualizada@test.com",
                "Nueva Calle 456",
                "987654321"
        );

        ClientDTO updatedClientDTO = new ClientDTO(
                "1",
                "María Actualizada",
                "García Actualizada",
                "12345678",
                "maria.actualizada@test.com",
                "Nueva Calle 456",
                "987654321"
        );

        when(clientService.update("1", updateDTO)).thenReturn(updatedClientDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("María Actualizada"))
                .andExpect(jsonPath("$.email").value("maria.actualizada@test.com"));

        // Verificar que se llamó al servicio
        verify(clientService).update("1", updateDTO);
    }

    /**
     * Test: Eliminar cliente exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint DELETE /api/clients/{id} funciona correctamente:
     * 1. Se envía una petición HTTP DELETE con el ID del cliente
     * 2. Se retorna una respuesta HTTP 204 (No Content)
     * 3. No hay contenido en el cuerpo de la respuesta
     */
    @Test
    void delete_ShouldReturnNoContent_WhenClientExistsViaHttp() throws Exception {
        // Arrange: Configurar que el servicio elimine sin problemas
        doNothing().when(clientService).delete("1");

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());

        // Verificar que se llamó al servicio
        verify(clientService).delete("1");
    }
}
