package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.SparePart;
import co.edu.uniquindio.serviautosbackend.domain.models.StockMovement;
import co.edu.uniquindio.serviautosbackend.dto.SparePartCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.SparePartDTO;
import co.edu.uniquindio.serviautosbackend.dto.SparePartUpdateDTO;
import co.edu.uniquindio.serviautosbackend.dto.StockMovementDTO;
import co.edu.uniquindio.serviautosbackend.repository.SparePartRepository;
import co.edu.uniquindio.serviautosbackend.repository.StockMovementRepository;
import co.edu.uniquindio.serviautosbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private SparePartRepository sparePartRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Override
    public SparePartDTO createSparePart(SparePartCreationDTO dto) {
        SparePart sparePart = new SparePart();
        sparePart.setName(dto.name());
        sparePart.setDetail(dto.detail());
        sparePart.setCategory(dto.category());
        sparePart.setBrand(dto.brand());
        sparePart.setPartNumber(dto.partNumber());
        sparePart.setUnitValue(dto.unitValue());
        sparePart.setAvailableStock(dto.availableStock());
        sparePart.setMinimumStock(dto.minimumStock());
        sparePart.setLocation(dto.location());

        SparePart saved = sparePartRepository.save(sparePart);
        return mapToDTO(saved);
    }

    @Override
    public SparePartDTO getSparePartById(String id) {
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        return mapToDTO(sparePart);
    }

    @Override
    public List<SparePartDTO> getAllSpareParts() {
        return sparePartRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<SparePartDTO> getSparePartsByCategory(String category) {
        return sparePartRepository.findByCategory(category)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<SparePartDTO> getSparePartsByBrand(String brand) {
        return sparePartRepository.findByBrand(brand)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<SparePartDTO> getLowStockSpareParts() {
        return sparePartRepository.findAll()
                .stream()
                .filter(sparePart -> sparePart.getAvailableStock() <= sparePart.getMinimumStock())
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<SparePartDTO> searchSparePartsByName(String name) {
        return sparePartRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public SparePartDTO updateSparePart(String id, SparePartUpdateDTO dto) {
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        sparePart.setName(dto.name());
        sparePart.setDetail(dto.detail());
        sparePart.setCategory(dto.category());
        sparePart.setBrand(dto.brand());
        sparePart.setPartNumber(dto.partNumber());
        sparePart.setUnitValue(dto.unitValue());
        sparePart.setAvailableStock(dto.availableStock());
        sparePart.setMinimumStock(dto.minimumStock());
        sparePart.setLocation(dto.location());

        SparePart updated = sparePartRepository.save(sparePart);
        return mapToDTO(updated);
    }

    @Override
    public void deleteSparePart(String id) {
        sparePartRepository.deleteById(id);
    }

    @Override
    public SparePartDTO addStock(String sparePartId, Integer quantity, String reason, String userId) {
        SparePart sparePart = sparePartRepository.findById(sparePartId)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        sparePart.setAvailableStock(sparePart.getAvailableStock() + quantity);
        SparePart updated = sparePartRepository.save(sparePart);

        // Registrar movimiento
        createStockMovement(sparePartId, "ENTRY", quantity, reason, userId);

        return mapToDTO(updated);
    }

    @Override
    public SparePartDTO removeStock(String sparePartId, Integer quantity, String reason, String userId) {
        SparePart sparePart = sparePartRepository.findById(sparePartId)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        if (sparePart.getAvailableStock() < quantity) {
            throw new RuntimeException("Stock insuficiente");
        }

        sparePart.setAvailableStock(sparePart.getAvailableStock() - quantity);
        SparePart updated = sparePartRepository.save(sparePart);

        // Registrar movimiento
        createStockMovement(sparePartId, "EXIT", quantity, reason, userId);

        return mapToDTO(updated);
    }

    @Override
    public SparePartDTO adjustStock(String sparePartId, Integer newQuantity, String reason, String userId) {
        SparePart sparePart = sparePartRepository.findById(sparePartId)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        Integer oldQuantity = sparePart.getAvailableStock();
        sparePart.setAvailableStock(newQuantity);
        SparePart updated = sparePartRepository.save(sparePart);

        // Registrar movimiento
        createStockMovement(sparePartId, "ADJUSTMENT", newQuantity - oldQuantity, reason, userId);

        return mapToDTO(updated);
    }

    @Override
    public List<StockMovementDTO> getStockMovements(String sparePartId) {
        return stockMovementRepository.findBySparePartId(sparePartId)
                .stream()
                .map(this::mapToMovementDTO)
                .toList();
    }

    @Override
    public List<StockMovementDTO> getAllStockMovements() {
        return stockMovementRepository.findAll()
                .stream()
                .map(this::mapToMovementDTO)
                .toList();
    }

    private void createStockMovement(String sparePartId, String movementType, Integer quantity, String reason, String userId) {
        StockMovement movement = new StockMovement();
        movement.setSparePartId(sparePartId);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReason(reason);
        movement.setDate(LocalDateTime.now());
        movement.setUserId(userId);
        stockMovementRepository.save(movement);
    }

    private SparePartDTO mapToDTO(SparePart sparePart) {
        return new SparePartDTO(
                sparePart.getId(),
                sparePart.getName(),
                sparePart.getDetail(),
                sparePart.getCategory(),
                sparePart.getBrand(),
                sparePart.getPartNumber(),
                sparePart.getUnitValue(),
                sparePart.getAvailableStock(),
                sparePart.getMinimumStock(),
                sparePart.getLocation()
        );
    }

    private StockMovementDTO mapToMovementDTO(StockMovement movement) {
        return new StockMovementDTO(
                movement.getId(),
                movement.getSparePartId(),
                movement.getMovementType(),
                movement.getQuantity(),
                movement.getReason(),
                movement.getDate(),
                movement.getUserId()
        );
    }
}
