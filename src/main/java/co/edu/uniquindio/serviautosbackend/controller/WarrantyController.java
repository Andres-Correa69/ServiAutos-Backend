package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.ResponseDTO;
import co.edu.uniquindio.serviautosbackend.dto.WarrantyDTO;
import co.edu.uniquindio.serviautosbackend.dto.WarrantyUpdateDTO;
import co.edu.uniquindio.serviautosbackend.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warranties")
@RequiredArgsConstructor
public class WarrantyController {
    
    @Autowired
    private WarrantyService warrantyService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<WarrantyDTO>> getById(@PathVariable String id) {
        try {
            WarrantyDTO warranty = warrantyService.getWarrantyById(id);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantía encontrada", warranty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @GetMapping("/order/{serviceOrderId}")
    public ResponseEntity<ResponseDTO<WarrantyDTO>> getByServiceOrderId(@PathVariable String serviceOrderId) {
        try {
            WarrantyDTO warranty = warrantyService.getWarrantyByServiceOrderId(serviceOrderId);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantía encontrada", warranty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @GetMapping
    public ResponseEntity<ResponseDTO<List<WarrantyDTO>>> getAll() {
        try {
            List<WarrantyDTO> warranties = warrantyService.getAllWarranties();
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantías obtenidas", warranties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ResponseDTO<List<WarrantyDTO>>> getByClientId(@PathVariable String clientId) {
        try {
            List<WarrantyDTO> warranties = warrantyService.getWarrantiesByClientId(clientId);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantías obtenidas", warranties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDTO<List<WarrantyDTO>>> getByStatus(@PathVariable String status) {
        try {
            List<WarrantyDTO> warranties = warrantyService.getWarrantiesByStatus(status);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantías obtenidas", warranties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<ResponseDTO<List<WarrantyDTO>>> getActive() {
        try {
            List<WarrantyDTO> warranties = warrantyService.getActiveWarranties();
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantías activas obtenidas", warranties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @GetMapping("/expired")
    public ResponseEntity<ResponseDTO<List<WarrantyDTO>>> getExpired() {
        try {
            List<WarrantyDTO> warranties = warrantyService.getExpiredWarranties();
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantías vencidas obtenidas", warranties));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<WarrantyDTO>> update(@PathVariable String id, 
                                                           @RequestBody WarrantyUpdateDTO dto) {
        try {
            WarrantyDTO warranty = warrantyService.updateWarranty(id, dto);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantía actualizada", warranty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}/attend")
    public ResponseEntity<ResponseDTO<WarrantyDTO>> attend(@PathVariable String id,
                                                           @RequestParam String technicianId,
                                                           @RequestParam(required = false) String observations) {
        try {
            WarrantyDTO warranty = warrantyService.attendWarranty(id, technicianId, observations);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantía atendida", warranty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}/close")
    public ResponseEntity<ResponseDTO<WarrantyDTO>> close(@PathVariable String id,
                                                          @RequestParam(required = false) String observations) {
        try {
            WarrantyDTO warranty = warrantyService.closeWarranty(id, observations);
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantía cerrada", warranty));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
    
    @PostMapping("/check-expired")
    public ResponseEntity<ResponseDTO<String>> checkExpired() {
        try {
            warrantyService.checkAndCloseExpiredWarranties();
            return ResponseEntity.ok(new ResponseDTO<>(false, "Garantías vencidas verificadas y cerradas", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }
}

