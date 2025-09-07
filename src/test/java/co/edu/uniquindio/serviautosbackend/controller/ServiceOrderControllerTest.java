package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.domain.models.Status;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderDTO;
import co.edu.uniquindio.serviautosbackend.service.ServiceOrderService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas unitarias para ServiceOrderController
 * 
 * Esta clase prueba todos los endpoints del controlador de órdenes de servicio:
 * - Crear orden de servicio (POST /api/orders)
 * - Obtener orden por ID (GET /api/orders/{id})
 * - Obtener todas las órdenes (GET /api/orders)
 * - Actualizar orden (PUT /api/orders/{id})
 * - Eliminar orden (DELETE /api/orders/{id})
 * 
 * Incluye dos tipos de pruebas:
 * 1. Pruebas unitarias directas del controlador
 * 2. Pruebas de integración con MockMvc para simular peticiones HTTP
 * 
 * Utiliza Mockito para simular el ServiceOrderService
 */
@ExtendWith(MockitoExtension.class)
class ServiceOrderControllerTest {

    // Mock del servicio de órdenes de servicio
    @Mock
    private ServiceOrderService serviceOrderService;

    // Inyección del controlador real con el mock
    @InjectMocks
    private ServiceOrderController serviceOrderController;

    // Objetos para pruebas HTTP con MockMvc
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    // Objetos de prueba reutilizables
    private ServiceOrderCreationDTO serviceOrderCreationDTO;
    private ServiceOrderDTO serviceOrderDTO;
    private LocalDateTime testDateTime;

    /**
     * Configuración inicial para cada test
     * Se ejecuta antes de cada método de prueba
     */
    @BeforeEach
    void setUp() {
        // Configurar MockMvc para pruebas HTTP
        mockMvc = MockMvcBuilders.standaloneSetup(serviceOrderController).build();
        objectMapper = new ObjectMapper();

        // Crear fecha de prueba
        testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30);

        // Crear datos de creación de orden de servicio de prueba
        serviceOrderCreationDTO = new ServiceOrderCreationDTO(
                "client1",
                "vehicle1",
                "Revisión general del motor",
                "Juan Pérez",
                150000.0
        );

        // Crear DTO de orden de servicio de prueba
        serviceOrderDTO = new ServiceOrderDTO(
                "1",
                "client1",
                "vehicle1",
                "Revisión general del motor",
                "Juan Pérez",
                150000.0,
                testDateTime,
                Status.PENDING
        );
    }

    // ========== PRUEBAS UNITARIAS DIRECTAS DEL CONTROLADOR ==========

    /**
     * Test: Crear orden de servicio exitosa (prueba unitaria directa)
     * 
     * Verifica que cuando se proporcionan datos válidos para crear una orden:
     * 1. Se llama al servicio con los datos correctos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la orden creada
     * 4. Todos los campos de la orden son correctos
     */
    @Test
    void create_ShouldReturnServiceOrderDTO_WhenValidData() throws Exception {
        // Arrange: Configurar que el servicio retorne la orden creada
        when(serviceOrderService.createOrder(any(ServiceOrderCreationDTO.class))).thenReturn(serviceOrderDTO);

        // Act: Crear la orden llamando directamente al controlador
        ResponseEntity<ServiceOrderDTO> response = serviceOrderController.create(serviceOrderCreationDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ServiceOrderDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("1", responseBody.id());
        assertEquals("client1", responseBody.clientId());
        assertEquals("vehicle1", responseBody.vehicleId());
        assertEquals("Revisión general del motor", responseBody.diagnostic());
        assertEquals("Juan Pérez", responseBody.assignedTechnician());
        assertEquals(150000.0, responseBody.laborValue());
        assertEquals(testDateTime, responseBody.dateService());
        assertEquals(Status.PENDING, responseBody.status());

        // Verificar que se llamó al servicio con los datos correctos
        verify(serviceOrderService).createOrder(serviceOrderCreationDTO);
    }

    /**
     * Test: Obtener orden por ID exitosa (prueba unitaria directa)
     * 
     * Verifica que cuando se busca una orden existente:
     * 1. Se llama al servicio con el ID correcto
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la orden encontrada
     */
    @Test
    void getById_ShouldReturnServiceOrderDTO_WhenOrderExists() throws Exception {
        // Arrange: Configurar que el servicio retorne la orden
        when(serviceOrderService.getOrderById("1")).thenReturn(serviceOrderDTO);

        // Act: Buscar la orden por ID
        ResponseEntity<ServiceOrderDTO> response = serviceOrderController.getById("1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ServiceOrderDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("1", responseBody.id());
        assertEquals("client1", responseBody.clientId());
        assertEquals("Revisión general del motor", responseBody.diagnostic());

        // Verificar que se llamó al servicio con el ID correcto
        verify(serviceOrderService).getOrderById("1");
    }

    /**
     * Test: Obtener todas las órdenes exitoso (prueba unitaria directa)
     * 
     * Verifica que cuando se solicitan todas las órdenes:
     * 1. Se llama al servicio para obtener la lista
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la lista de órdenes
     * 4. La lista tiene el número correcto de elementos
     */
    @Test
    void getAll_ShouldReturnListOfServiceOrderDTOs_WhenOrdersExist() throws Exception {
        // Arrange: Crear una lista de órdenes de prueba
        ServiceOrderDTO order2 = new ServiceOrderDTO(
                "2",
                "client2",
                "vehicle2",
                "Cambio de aceite",
                "María García",
                50000.0,
                testDateTime.plusDays(1),
                Status.FINALIZED
        );

        List<ServiceOrderDTO> orders = Arrays.asList(serviceOrderDTO, order2);
        when(serviceOrderService.getAllOrders()).thenReturn(orders);

        // Act: Obtener todas las órdenes
        ResponseEntity<List<ServiceOrderDTO>> response = serviceOrderController.getAll();

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<ServiceOrderDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertEquals("Revisión general del motor", responseBody.get(0).diagnostic());
        assertEquals("Cambio de aceite", responseBody.get(1).diagnostic());

        // Verificar que se llamó al servicio
        verify(serviceOrderService).getAllOrders();
    }

    /**
     * Test: Actualizar orden exitosa (prueba unitaria directa)
     * 
     * Verifica que cuando se actualiza una orden existente:
     * 1. Se llama al servicio con el ID y datos correctos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El cuerpo de la respuesta contiene la orden actualizada
     * 4. Los campos actualizados son correctos
     */
    @Test
    void update_ShouldReturnUpdatedServiceOrderDTO_WhenOrderExists() throws Exception {
        // Arrange: Crear datos de actualización y orden actualizada
        ServiceOrderCreationDTO updateDTO = new ServiceOrderCreationDTO(
                "client1",
                "vehicle1",
                "Revisión completa del motor y transmisión",
                "Carlos López",
                200000.0
        );

        ServiceOrderDTO updatedOrderDTO = new ServiceOrderDTO(
                "1",
                "client1",
                "vehicle1",
                "Revisión completa del motor y transmisión",
                "Carlos López",
                200000.0,
                testDateTime,
                Status.PENDING
        );

        when(serviceOrderService.updateOrder("1", updateDTO)).thenReturn(updatedOrderDTO);

        // Act: Actualizar la orden
        ResponseEntity<ServiceOrderDTO> response = serviceOrderController.update("1", updateDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ServiceOrderDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("1", responseBody.id());
        assertEquals("Revisión completa del motor y transmisión", responseBody.diagnostic());
        assertEquals("Carlos López", responseBody.assignedTechnician());
        assertEquals(200000.0, responseBody.laborValue());

        // Verificar que se llamó al servicio con los parámetros correctos
        verify(serviceOrderService).updateOrder("1", updateDTO);
    }

    /**
     * Test: Eliminar orden exitosa (prueba unitaria directa)
     * 
     * Verifica que cuando se elimina una orden:
     * 1. Se llama al servicio con el ID correcto
     * 2. Se retorna una respuesta HTTP 204 (No Content)
     * 3. No hay contenido en el cuerpo de la respuesta
     */
    @Test
    void delete_ShouldReturnNoContent_WhenOrderExists() throws Exception {
        // Arrange: Configurar que el servicio elimine sin problemas
        doNothing().when(serviceOrderService).deleteOrder("1");

        // Act: Eliminar la orden
        ResponseEntity<Void> response = serviceOrderController.delete("1");

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verificar que se llamó al servicio con el ID correcto
        verify(serviceOrderService).deleteOrder("1");
    }

    // ========== PRUEBAS DE INTEGRACIÓN CON MOCKMVC ==========

    /**
     * Test: Crear orden de servicio exitosa vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint POST /api/orders funciona correctamente:
     * 1. Se envía una petición HTTP POST con datos JSON válidos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos correctos de la orden
     * 4. Se verifica cada campo individualmente usando jsonPath
     */
    @Test
    void create_ShouldReturnServiceOrderDTO_WhenValidDataViaHttp() throws Exception {
        // Arrange: Configurar que el servicio retorne la orden creada
        when(serviceOrderService.createOrder(any(ServiceOrderCreationDTO.class))).thenReturn(serviceOrderDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceOrderCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.clientId").value("client1"))
                .andExpect(jsonPath("$.vehicleId").value("vehicle1"))
                .andExpect(jsonPath("$.diagnostic").value("Revisión general del motor"))
                .andExpect(jsonPath("$.assignedTechnician").value("Juan Pérez"))
                .andExpect(jsonPath("$.laborValue").value(150000.0))
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Verificar que se llamó al servicio
        verify(serviceOrderService).createOrder(any(ServiceOrderCreationDTO.class));
    }

    /**
     * Test: Obtener orden por ID exitosa vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/orders/{id} funciona correctamente:
     * 1. Se envía una petición HTTP GET con el ID de la orden
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos correctos de la orden
     */
    @Test
    void getById_ShouldReturnServiceOrderDTO_WhenOrderExistsViaHttp() throws Exception {
        // Arrange: Configurar que el servicio retorne la orden
        when(serviceOrderService.getOrderById("1")).thenReturn(serviceOrderDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.clientId").value("client1"))
                .andExpect(jsonPath("$.vehicleId").value("vehicle1"))
                .andExpect(jsonPath("$.diagnostic").value("Revisión general del motor"))
                .andExpect(jsonPath("$.assignedTechnician").value("Juan Pérez"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Verificar que se llamó al servicio
        verify(serviceOrderService).getOrderById("1");
    }

    /**
     * Test: Obtener todas las órdenes exitoso vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint GET /api/orders funciona correctamente:
     * 1. Se envía una petición HTTP GET sin parámetros
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta es un array con las órdenes correctas
     * 4. Se verifica el tamaño del array y los datos de cada elemento
     */
    @Test
    void getAll_ShouldReturnListOfServiceOrderDTOs_WhenOrdersExistViaHttp() throws Exception {
        // Arrange: Crear lista de órdenes y configurar servicio
        ServiceOrderDTO order2 = new ServiceOrderDTO(
                "2",
                "client2",
                "vehicle2",
                "Cambio de aceite",
                "María García",
                50000.0,
                testDateTime.plusDays(1),
                Status.FINALIZED
        );

        List<ServiceOrderDTO> orders = Arrays.asList(serviceOrderDTO, order2);
        when(serviceOrderService.getAllOrders()).thenReturn(orders);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].diagnostic").value("Revisión general del motor"))
                .andExpect(jsonPath("$[1].diagnostic").value("Cambio de aceite"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("FINALIZED"));

        // Verificar que se llamó al servicio
        verify(serviceOrderService).getAllOrders();
    }

    /**
     * Test: Actualizar orden exitosa vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint PUT /api/orders/{id} funciona correctamente:
     * 1. Se envía una petición HTTP PUT con datos JSON válidos
     * 2. Se retorna una respuesta HTTP 200 (OK)
     * 3. El JSON de respuesta contiene los datos actualizados de la orden
     */
    @Test
    void update_ShouldReturnUpdatedServiceOrderDTO_WhenOrderExistsViaHttp() throws Exception {
        // Arrange: Crear datos de actualización y orden actualizada
        ServiceOrderCreationDTO updateDTO = new ServiceOrderCreationDTO(
                "client1",
                "vehicle1",
                "Revisión completa del motor y transmisión",
                "Carlos López",
                200000.0
        );

        ServiceOrderDTO updatedOrderDTO = new ServiceOrderDTO(
                "1",
                "client1",
                "vehicle1",
                "Revisión completa del motor y transmisión",
                "Carlos López",
                200000.0,
                testDateTime,
                Status.PENDING
        );

        when(serviceOrderService.updateOrder("1", updateDTO)).thenReturn(updatedOrderDTO);

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.diagnostic").value("Revisión completa del motor y transmisión"))
                .andExpect(jsonPath("$.assignedTechnician").value("Carlos López"))
                .andExpect(jsonPath("$.laborValue").value(200000.0));

        // Verificar que se llamó al servicio
        verify(serviceOrderService).updateOrder("1", updateDTO);
    }

    /**
     * Test: Eliminar orden exitosa vía HTTP (prueba de integración)
     * 
     * Verifica que el endpoint DELETE /api/orders/{id} funciona correctamente:
     * 1. Se envía una petición HTTP DELETE con el ID de la orden
     * 2. Se retorna una respuesta HTTP 204 (No Content)
     * 3. No hay contenido en el cuerpo de la respuesta
     */
    @Test
    void delete_ShouldReturnNoContent_WhenOrderExistsViaHttp() throws Exception {
        // Arrange: Configurar que el servicio elimine sin problemas
        doNothing().when(serviceOrderService).deleteOrder("1");

        // Act & Assert: Realizar petición HTTP y verificar respuesta
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        // Verificar que se llamó al servicio
        verify(serviceOrderService).deleteOrder("1");
    }

    /**
     * Test: Crear orden con diferentes estados (prueba adicional)
     * 
     * Verifica que se pueden crear órdenes con diferentes estados:
     * 1. Orden pendiente (PENDING)
     * 2. Orden finalizada (FINALIZED)
     */
    @Test
    void create_ShouldHandleDifferentStatuses_WhenCreatingOrders() throws Exception {
        // Arrange: Crear orden finalizada
        ServiceOrderDTO finalizedOrder = new ServiceOrderDTO(
                "2",
                "client2",
                "vehicle2",
                "Cambio de aceite completado",
                "María García",
                50000.0,
                testDateTime,
                Status.FINALIZED
        );

        when(serviceOrderService.createOrder(any(ServiceOrderCreationDTO.class))).thenReturn(finalizedOrder);

        // Act: Crear la orden
        ResponseEntity<ServiceOrderDTO> response = serviceOrderController.create(serviceOrderCreationDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ServiceOrderDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(Status.FINALIZED, responseBody.status());
        assertEquals("Cambio de aceite completado", responseBody.diagnostic());

        // Verificar que se llamó al servicio
        verify(serviceOrderService).createOrder(serviceOrderCreationDTO);
    }

    /**
     * Test: Verificar campos numéricos en órdenes (prueba adicional)
     * 
     * Verifica que los valores numéricos se manejan correctamente:
     * 1. Valores de trabajo positivos
     * 2. Valores decimales
     */
    @Test
    void create_ShouldHandleNumericValues_WhenCreatingOrders() throws Exception {
        // Arrange: Crear orden con valores decimales
        ServiceOrderCreationDTO decimalOrderDTO = new ServiceOrderCreationDTO(
                "client1",
                "vehicle1",
                "Revisión con valores decimales",
                "Juan Pérez",
                150000.50
        );

        ServiceOrderDTO decimalOrder = new ServiceOrderDTO(
                "1",
                "client1",
                "vehicle1",
                "Revisión con valores decimales",
                "Juan Pérez",
                150000.50,
                testDateTime,
                Status.PENDING
        );

        when(serviceOrderService.createOrder(any(ServiceOrderCreationDTO.class))).thenReturn(decimalOrder);

        // Act: Crear la orden
        ResponseEntity<ServiceOrderDTO> response = serviceOrderController.create(decimalOrderDTO);

        // Assert: Verificar que la respuesta es correcta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ServiceOrderDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(150000.50, responseBody.laborValue());

        // Verificar que se llamó al servicio
        verify(serviceOrderService).createOrder(decimalOrderDTO);
    }
}
