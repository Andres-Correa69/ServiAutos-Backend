package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderDTO;
import co.edu.uniquindio.serviautosbackend.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
