package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.WarrantyDTO;
import co.edu.uniquindio.serviautosbackend.dto.WarrantyUpdateDTO;
import java.util.List;

public interface WarrantyService {
    
    WarrantyDTO createWarranty(String serviceOrderId, String clientId, String vehicleId, 
                               Integer warrantyDays, String description);
    
    WarrantyDTO getWarrantyById(String id);
    
    WarrantyDTO getWarrantyByServiceOrderId(String serviceOrderId);
    
    List<WarrantyDTO> getAllWarranties();
    
    List<WarrantyDTO> getWarrantiesByClientId(String clientId);
    
    List<WarrantyDTO> getWarrantiesByStatus(String status);
    
    List<WarrantyDTO> getActiveWarranties(); // OPEN y no vencidas
    
    List<WarrantyDTO> getExpiredWarranties(); // Vencidas pero no cerradas
    
    WarrantyDTO updateWarranty(String id, WarrantyUpdateDTO dto);
    
    WarrantyDTO attendWarranty(String id, String technicianId, String observations);
    
    WarrantyDTO closeWarranty(String id, String observations);
    
    void checkAndCloseExpiredWarranties(); // Para ejecutar periódicamente
}

