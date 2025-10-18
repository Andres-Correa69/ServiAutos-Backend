package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.SparePartCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.SparePartDTO;
import co.edu.uniquindio.serviautosbackend.dto.SparePartUpdateDTO;
import co.edu.uniquindio.serviautosbackend.dto.StockMovementDTO;
import co.edu.uniquindio.serviautosbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Operaciones CRUD para repuestos
    @PostMapping("/spare-parts")
    public ResponseEntity<SparePartDTO> createSparePart(@RequestBody SparePartCreationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createSparePart(dto));
    }

    @GetMapping("/spare-parts/{id}")
    public ResponseEntity<SparePartDTO> getSparePartById(@PathVariable String id) {
        return ResponseEntity.ok(inventoryService.getSparePartById(id));
    }

    @GetMapping("/spare-parts")
    public ResponseEntity<List<SparePartDTO>> getAllSpareParts() {
        return ResponseEntity.ok(inventoryService.getAllSpareParts());
    }

    @GetMapping("/spare-parts/category/{category}")
    public ResponseEntity<List<SparePartDTO>> getSparePartsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(inventoryService.getSparePartsByCategory(category));
    }

    @GetMapping("/spare-parts/brand/{brand}")
    public ResponseEntity<List<SparePartDTO>> getSparePartsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(inventoryService.getSparePartsByBrand(brand));
    }

    @GetMapping("/spare-parts/low-stock")
    public ResponseEntity<List<SparePartDTO>> getLowStockSpareParts() {
        return ResponseEntity.ok(inventoryService.getLowStockSpareParts());
    }

    @GetMapping("/spare-parts/search")
    public ResponseEntity<List<SparePartDTO>> searchSparePartsByName(@RequestParam String name) {
        return ResponseEntity.ok(inventoryService.searchSparePartsByName(name));
    }

    @PutMapping("/spare-parts/{id}")
    public ResponseEntity<SparePartDTO> updateSparePart(@PathVariable String id, @RequestBody SparePartUpdateDTO dto) {
        return ResponseEntity.ok(inventoryService.updateSparePart(id, dto));
    }

    @DeleteMapping("/spare-parts/{id}")
    public ResponseEntity<Void> deleteSparePart(@PathVariable String id) {
        inventoryService.deleteSparePart(id);
        return ResponseEntity.noContent().build();
    }

    // Operaciones de stock
    @PostMapping("/spare-parts/{id}/add-stock")
    public ResponseEntity<SparePartDTO> addStock(
            @PathVariable String id,
            @RequestParam Integer quantity,
            @RequestParam String reason,
            @RequestParam String userId) {
        return ResponseEntity.ok(inventoryService.addStock(id, quantity, reason, userId));
    }

    @PostMapping("/spare-parts/{id}/remove-stock")
    public ResponseEntity<SparePartDTO> removeStock(
            @PathVariable String id,
            @RequestParam Integer quantity,
            @RequestParam String reason,
            @RequestParam String userId) {
        return ResponseEntity.ok(inventoryService.removeStock(id, quantity, reason, userId));
    }

    @PostMapping("/spare-parts/{id}/adjust-stock")
    public ResponseEntity<SparePartDTO> adjustStock(
            @PathVariable String id,
            @RequestParam Integer newQuantity,
            @RequestParam String reason,
            @RequestParam String userId) {
        return ResponseEntity.ok(inventoryService.adjustStock(id, newQuantity, reason, userId));
    }

    // Historial de movimientos
    @GetMapping("/spare-parts/{id}/movements")
    public ResponseEntity<List<StockMovementDTO>> getStockMovements(@PathVariable String id) {
        return ResponseEntity.ok(inventoryService.getStockMovements(id));
    }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovementDTO>> getAllStockMovements() {
        return ResponseEntity.ok(inventoryService.getAllStockMovements());
    }
}
