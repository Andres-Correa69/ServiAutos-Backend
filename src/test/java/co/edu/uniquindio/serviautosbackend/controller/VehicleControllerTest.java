package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.VehicleCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.VehicleDTO;
import co.edu.uniquindio.serviautosbackend.service.VehicleService;
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
 * Clase de pruebas unitarias para VehicleController
 * 
 * Esta clase prueba todos los endpoints del controlador de vehículos:
 * - Crear vehículo (POST /api/vehicles)
 * - Obtener vehículo por ID (GET /api/vehicles/{id})
 * - Obtener todos los vehículos (GET /api/vehicles)
 * - Actualizar vehículo (PUT /api/vehicles/{id})
 * - Eliminar vehículo (DELETE /api/vehicles/{id})
 * - Obtener vehículos por cliente (GET /api/vehicles/client/{clientId})
 * 
 * Incluye dos tipos de pruebas:
 * 1. Pruebas unitarias directas del controlador
 * 2. Pruebas de integración con MockMvc para simular peticiones HTTP
 * 
 * Utiliza Mockito para simular el VehicleService
 */
@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    // Mock del servicio de vehículos
    @Mock
    private VehicleService vehicleService;

    // Inyección del controlador real con el mock
    @InjectMocks
    private VehicleController vehicleController;

    // Objetos para pruebas HTTP con MockMvc
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    // Objetos de prueba reutilizables
    private VehicleCreationDTO vehicleCreationDTO;
    private VehicleDTO vehicleDTO;

    /**
     * Configuración inicial para cada test
     * Se ejecuta antes de cada método de prueba
     */
    @BeforeEach
    void setUp() {
        // Configurar MockMvc para pruebas HTTP
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        objectMapper = new ObjectMapper();

        // Crear datos de creación de vehículo de prueba
        vehicleCreationDTO = new VehicleCreationDTO(
                "ABC123",
                "Toyota",
                "Corolla",
                "client1"
        );

        // Crear DTO de vehículo de prueba
        vehicleDTO = new VehicleDTO(
                "1",
                "ABC123",
                "Toyota",
                "Corolla",
                "client1"
        );
    }

    // ========== PRUEBAS UNITARIAS DIRECTAS DEL CONTROLADOR ==========

    /**
     * Test: Crear vehículo exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se proporcionan datos válidos para crear un vehículo:
     * 1. Se llama al servicio con los datos correctos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene el vehículo creado
     * 4. Todos los campos del vehículo son correctos
     */
    @Test
    void create_ShouldReturnVehicleDTO_WhenValidData() throws Exception {
        // Arrange: Configurar que el servicio retorne el vehículo creado
        when(vehicleService.create(any(VehicleCreationDTO.class))).thenReturn(vehicleDTO);

        // Act: Crear el vehículo llamando directamente al controlador
        ResponseEntity<VehicleDTO> response = vehicleController.create(vehicleCreationDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().id());
        assertEquals("ABC123", response.getBody().licencePlate());
        assertEquals("Toyota", response.getBody().brand());
        assertEquals("Corolla", response.getBody().model());
        assertEquals("client1", response.getBody().clientId());

        // Verificar que se llamó al servicio con los datos correctos
        verify(vehicleService).create(vehicleCreationDTO);
    }

    /**
     * Test: Obtener vehículo por ID exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se busca un vehículo existente:
     * 1. Se llama al servicio con el ID correcto
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene el vehículo encontrado
     */
    @Test
    void getById_ShouldReturnVehicleDTO_WhenVehicleExists() throws Exception {
        // Arrange: Configurar que el servicio retorne el vehículo
        when(vehicleService.getById("1")).thenReturn(vehicleDTO);

        // Act: Buscar el vehículo por ID
        ResponseEntity<VehicleDTO> response = vehicleController.getById("1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().id());
        assertEquals("ABC123", response.getBody().licencePlate());
        assertEquals("Toyota", response.getBody().brand());

        // Verificar que se llamó al servicio con el ID correcto
        verify(vehicleService).getById("1");
    }

    /**
     * Test: Obtener todos los vehículos exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se solicitan todos los vehículos:
     * 1. Se llama al servicio para obtener la lista
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la lista de vehículos
     * 4. La lista tiene el número correcto de elementos
     */
    @Test
    void getAll_ShouldReturnListOfVehicleDTOs_WhenVehiclesExist() throws Exception {
        // Arrange: Crear una lista de vehículos de prueba
        VehicleDTO vehicle2 = new VehicleDTO(
                "2",
                "XYZ789",
                "Honda",
                "Civic",
                "client2"
        );

        List<VehicleDTO> vehicles = Arrays.asList(vehicleDTO, vehicle2);
        when(vehicleService.getAll()).thenReturn(vehicles);

        // Act: Obtener todos los vehículos
        ResponseEntity<List<VehicleDTO>> response = vehicleController.getAll();

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Toyota", response.getBody().get(0).brand());
        assertEquals("Honda", response.getBody().get(1).brand());

        // Verificar que se llamó al servicio
        verify(vehicleService).getAll();
    }

    /**
     * Test: Actualizar vehículo exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se actualiza un vehículo existente:
     * 1. Se llama al servicio con el ID y datos correctos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene el vehículo actualizado
     * 4. Los campos actualizados son correctos
     */
    @Test
    void update_ShouldReturnUpdatedVehicleDTO_WhenVehicleExists() throws Exception {
        // Arrange: Crear datos de actualización y vehículo actualizado
        VehicleCreationDTO updateDTO = new VehicleCreationDTO(
                "ABC123",
                "Toyota",
                "Corolla Hybrid",
                "client1"
        );

        VehicleDTO updatedVehicleDTO = new VehicleDTO(
                "1",
                "ABC123",
                "Toyota",
                "Corolla Hybrid",
                "client1"
        );

        when(vehicleService.update("1", updateDTO)).thenReturn(updatedVehicleDTO);

        // Act: Actualizar el vehículo
        ResponseEntity<VehicleDTO> response = vehicleController.update("1", updateDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().id());
        assertEquals("ABC123", response.getBody().licencePlate());
        assertEquals("Corolla Hybrid", response.getBody().model());

        // Verificar que se llamó al servicio con los parámetros correctos
        verify(vehicleService).update("1", updateDTO);
    }

    /**
     * Test: Eliminar vehículo exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se elimina un vehículo:
     * 1. Se llama al servicio con el ID correcto
     * 2. Se retorna una respuesta HTTP 204 (No Content)
     * 3. No hay contenido en el cuerpo de la respuesta
     */
    @Test
    void delete_ShouldReturnNoContent_WhenVehicleExists() throws Exception {
        // Arrange: Configurar que el servicio elimine sin problemas
        doNothing().when(vehicleService).delete("1");

        // Act: Eliminar el vehículo
        ResponseEntity<Void> response = vehicleController.delete("1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verificar que se llamó al servicio con el ID correcto
        verify(vehicleService).delete("1");
    }

    /**
     * Test: Obtener vehículos por cliente exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se buscan vehículos de un cliente específico:
     * 1. Se llama al servicio con el clientId correcto
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la lista de vehículos del cliente
     * 4. Todos los vehículos pertenecen al cliente correcto
     */
    @Test
    void getByClientId_ShouldReturnListOfVehicleDTOs_WhenClientHasVehicles() throws Exception {
        // Arrange: Crear vehículos del mismo cliente
        VehicleDTO vehicle2 = new VehicleDTO(
                "2",
                "DEF456",
                "Toyota",
                "Camry",
                "client1"
        );

        List<VehicleDTO> clientVehicles = Arrays.asList(vehicleDTO, vehicle2);
        when(vehicleService.getByClientId("client1")).thenReturn(clientVehicles);

        // Act: Buscar vehículos por cliente
        ResponseEntity<List<VehicleDTO>> response = vehicleController.getByClientId("client1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("client1", response.getBody().get(0).clientId());
        assertEquals("client1", response.getBody().get(1).clientId());

        // Verificar que se llamó al servicio con el clientId correcto
        verify(vehicleService).getByClientId("client1");
    }

    // ========== PRUEBAS DE INTEGRACIÓN CON MOCKMVC ==========

    /**
     * Test: Crear vehículo exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint POST /api/vehicles funciona correctamente:
     * 1. Se envía una petición HTTP POST con datos JSON válidos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos correctos del vehículo
     * 4. Se verifica cada campo individualmente usando jsonPath
     */
    @Test
    void create_ShouldReturnVehicleDTO_WhenValidDataViaHttp() throws Exception {
        // Arrange: Configurar que el servicio retorne el vehículo creado
        when(vehicleService.create(any(VehicleCreationDTO.class))).thenReturn(vehicleDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.licencePlate").value("ABC123"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.clientId").value("client1"));

        // Verificar que se llamó al servicio
        verify(vehicleService).create(any(VehicleCreationDTO.class));
    }

    /**
     * Test: Obtener vehículo por ID exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/vehicles/{id} funciona correctamente:
     * 1. Se envía una petición HTTP GET con el ID del vehículo
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos correctos del vehículo
     */
    @Test
    void getById_ShouldReturnVehicleDTO_WhenVehicleExistsViaHttp() throws Exception {
        // Arrange: Configurar que el servicio retorne el vehículo
        when(vehicleService.getById("1")).thenReturn(vehicleDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.licencePlate").value("ABC123"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"));

        // Verificar que se llamó al servicio
        verify(vehicleService).getById("1");
    }

    /**
     * Test: Obtener todos los vehículos exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/vehicles funciona correctamente:
     * 1. Se envía una petición HTTP GET sin parámetros
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta es un array con los vehículos correctos
     * 4. Se verifica el tamaño del array y los datos de cada elemento
     */
    @Test
    void getAll_ShouldReturnListOfVehicleDTOs_WhenVehiclesExistViaHttp() throws Exception {
        // Arrange: Crear lista de vehículos y configurar servicio
        VehicleDTO vehicle2 = new VehicleDTO(
                "2",
                "XYZ789",
                "Honda",
                "Civic",
                "client2"
        );

        List<VehicleDTO> vehicles = Arrays.asList(vehicleDTO, vehicle2);
        when(vehicleService.getAll()).thenReturn(vehicles);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].brand").value("Honda"));

        // Verificar que se llamó al servicio
        verify(vehicleService).getAll();
    }

    /**
     * Test: Actualizar vehículo exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint PUT /api/vehicles/{id} funciona correctamente:
     * 1. Se envía una petición HTTP PUT con datos JSON válidos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos actualizados del vehículo
     */
    @Test
    void update_ShouldReturnUpdatedVehicleDTO_WhenVehicleExistsViaHttp() throws Exception {
        // Arrange: Crear datos de actualización y vehículo actualizado
        VehicleCreationDTO updateDTO = new VehicleCreationDTO(
                "ABC123",
                "Toyota",
                "Corolla Hybrid",
                "client1"
        );

        VehicleDTO updatedVehicleDTO = new VehicleDTO(
                "1",
                "ABC123",
                "Toyota",
                "Corolla Hybrid",
                "client1"
        );

        when(vehicleService.update("1", updateDTO)).thenReturn(updatedVehicleDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(put("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.licencePlate").value("ABC123"))
                .andExpect(jsonPath("$.model").value("Corolla Hybrid"));

        // Verificar que se llamó al servicio
        verify(vehicleService).update("1", updateDTO);
    }

    /**
     * Test: Eliminar vehículo exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint DELETE /api/vehicles/{id} funciona correctamente:
     * 1. Se envía una petición HTTP DELETE con el ID del vehículo
     * 2. Se retorna una respuesta HTTP 204 (No Content)
     * 3. No hay contenido en el cuerpo de la respuesta
     */
    @Test
    void delete_ShouldReturnNoContent_WhenVehicleExistsViaHttp() throws Exception {
        // Arrange: Configurar que el servicio elimine sin problemas
        doNothing().when(vehicleService).delete("1");

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isNoContent());

        // Verificar que se llamó al servicio
        verify(vehicleService).delete("1");
    }

    /**
     * Test: Obtener vehículos por cliente exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/vehicles/client/{clientId} funciona correctamente:
     * 1. Se envía una petición HTTP GET con el clientId
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta es un array con los vehículos del cliente
     * 4. Se verifica que todos los vehículos pertenecen al cliente correcto
     */
    @Test
    void getByClientId_ShouldReturnListOfVehicleDTOs_WhenClientHasVehiclesViaHttp() throws Exception {
        // Arrange: Crear vehículos del mismo cliente y configurar servicio
        VehicleDTO vehicle2 = new VehicleDTO(
                "2",
                "DEF456",
                "Toyota",
                "Camry",
                "client1"
        );

        List<VehicleDTO> clientVehicles = Arrays.asList(vehicleDTO, vehicle2);
        when(vehicleService.getByClientId("client1")).thenReturn(clientVehicles);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/vehicles/client/client1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientId").value("client1"))
                .andExpect(jsonPath("$[1].clientId").value("client1"));

        // Verificar que se llamó al servicio
        verify(vehicleService).getByClientId("client1");
    }
}