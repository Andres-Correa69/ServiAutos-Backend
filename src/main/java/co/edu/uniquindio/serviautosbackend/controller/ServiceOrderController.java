package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.AddSparePartToOrderDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderDTO;
import co.edu.uniquindio.serviautosbackend.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping
    public ResponseEntity<ServiceOrderDTO> create(@RequestBody ServiceOrderCreationDTO dto) {
        return ResponseEntity.ok(serviceOrderService.createOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(serviceOrderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrderDTO>> getAll() {
        return ResponseEntity.ok(serviceOrderService.getAllOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> update(@PathVariable String id, @RequestBody ServiceOrderCreationDTO dto) {
        return ResponseEntity.ok(serviceOrderService.updateOrder(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/attend")
    public ResponseEntity<ServiceOrderDTO> attendOrder(@PathVariable String id, @RequestParam String technicianId) {
        return ResponseEntity.ok(serviceOrderService.attendOrder(id, technicianId));
    }

    @PostMapping("/{id}/spare-parts")
    public ResponseEntity<?> addSparePartToOrder(@PathVariable String id, @RequestBody AddSparePartToOrderDTO dto) {
        try {
            // Debug: Log de los datos recibidos
            System.out.println("DEBUG - Order ID: " + id);
            System.out.println("DEBUG - DTO: " + dto);
            System.out.println("DEBUG - SparePartId: " + (dto != null ? dto.sparePartId() : "null"));
            System.out.println("DEBUG - Quantity: " + (dto != null ? dto.quantity() : "null"));
            
            ServiceOrderDTO result = serviceOrderService.addSparePartToOrder(id, dto);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            System.out.println("DEBUG - Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}/spare-parts/{sparePartId}")
    public ResponseEntity<?> removeSparePartFromOrder(@PathVariable String id, @PathVariable String sparePartId) {
        try {
            ServiceOrderDTO result = serviceOrderService.removeSparePartFromOrder(id, sparePartId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}/finalize")
    public ResponseEntity<?> finalizeOrder(@PathVariable String id) {
        try {
            ServiceOrderDTO result = serviceOrderService.finalizeOrder(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", true,
                "message", e.getMessage()
            ));
        }
    }

}
