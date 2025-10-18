package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.SparePartCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.SparePartDTO;
import co.edu.uniquindio.serviautosbackend.dto.SparePartUpdateDTO;
import co.edu.uniquindio.serviautosbackend.dto.StockMovementDTO;

import java.util.List;

public interface InventoryService {

    // Operaciones CRUD para repuestos
    SparePartDTO createSparePart(SparePartCreationDTO dto);
    SparePartDTO getSparePartById(String id);
    List<SparePartDTO> getAllSpareParts();
    List<SparePartDTO> getSparePartsByCategory(String category);
    List<SparePartDTO> getSparePartsByBrand(String brand);
    List<SparePartDTO> getLowStockSpareParts();
    List<SparePartDTO> searchSparePartsByName(String name);
    SparePartDTO updateSparePart(String id, SparePartUpdateDTO dto);
    void deleteSparePart(String id);

    // Operaciones de stock
    SparePartDTO addStock(String sparePartId, Integer quantity, String reason, String userId);
    SparePartDTO removeStock(String sparePartId, Integer quantity, String reason, String userId);
    SparePartDTO adjustStock(String sparePartId, Integer newQuantity, String reason, String userId);
    
    // Historial de movimientos
    List<StockMovementDTO> getStockMovements(String sparePartId);
    List<StockMovementDTO> getAllStockMovements();
}
