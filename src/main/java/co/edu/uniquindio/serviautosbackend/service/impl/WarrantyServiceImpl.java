package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.Warranty;
import co.edu.uniquindio.serviautosbackend.domain.models.WarrantyStatus;
import co.edu.uniquindio.serviautosbackend.dto.WarrantyDTO;
import co.edu.uniquindio.serviautosbackend.dto.WarrantyUpdateDTO;
import co.edu.uniquindio.serviautosbackend.repository.WarrantyRepository;
import co.edu.uniquindio.serviautosbackend.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarrantyServiceImpl implements WarrantyService {
    
    @Autowired
    private WarrantyRepository warrantyRepository;
    
    @Value("${warranty.default-days:30}")
    private Integer defaultWarrantyDays;
    
    @Override
    public WarrantyDTO createWarranty(String serviceOrderId, String clientId, String vehicleId,
                                      Integer warrantyDays, String description) {
        // Verificar que no exista ya una garantía para esta orden
        warrantyRepository.findByServiceOrderId(serviceOrderId)
            .ifPresent(w -> {
                throw new RuntimeException("Ya existe una garantía para esta orden de servicio");
            });
        
        LocalDateTime now = LocalDateTime.now();
        int days = warrantyDays != null ? warrantyDays : defaultWarrantyDays;
        LocalDateTime endDate = now.plusDays(days);
        
        Warranty warranty = new Warranty(
            UUID.randomUUID().toString(),
            serviceOrderId,
            clientId,
            vehicleId,
            WarrantyStatus.OPEN,
            now,
            endDate,
            now,
            now,
            description != null ? description : "Garantía automática creada al finalizar orden",
            null,
            null
        );
        
        Warranty saved = warrantyRepository.save(warranty);
        return mapToDTO(saved);
    }
    
    @Override
    public WarrantyDTO getWarrantyById(String id) {
        Warranty warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));
        return mapToDTO(warranty);
    }
    
    @Override
    public WarrantyDTO getWarrantyByServiceOrderId(String serviceOrderId) {
        Warranty warranty = warrantyRepository.findByServiceOrderId(serviceOrderId)
            .orElseThrow(() -> new RuntimeException("Garantía no encontrada para esta orden"));
        return mapToDTO(warranty);
    }
    
    @Override
    public List<WarrantyDTO> getAllWarranties() {
        return warrantyRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .toList();
    }
    
    @Override
    public List<WarrantyDTO> getWarrantiesByClientId(String clientId) {
        return warrantyRepository.findByClientId(clientId)
            .stream()
            .map(this::mapToDTO)
            .toList();
    }
    
    @Override
    public List<WarrantyDTO> getWarrantiesByStatus(String status) {
        WarrantyStatus warrantyStatus = WarrantyStatus.valueOf(status.toUpperCase());
        return warrantyRepository.findByStatus(warrantyStatus)
            .stream()
            .map(this::mapToDTO)
            .toList();
    }
    
    @Override
    public List<WarrantyDTO> getActiveWarranties() {
        LocalDateTime now = LocalDateTime.now();
        return warrantyRepository.findByStatusAndEndDateAfter(WarrantyStatus.OPEN, now)
            .stream()
            .map(this::mapToDTO)
            .toList();
    }
    
    @Override
    public List<WarrantyDTO> getExpiredWarranties() {
        LocalDateTime now = LocalDateTime.now();
        return warrantyRepository.findByEndDateBeforeAndStatusNot(now, WarrantyStatus.CLOSED)
            .stream()
            .map(this::mapToDTO)
            .toList();
    }
    
    @Override
    public WarrantyDTO updateWarranty(String id, WarrantyUpdateDTO dto) {
        Warranty warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));
        
        if (dto.status() != null) {
            warranty.setStatus(dto.status());
        }
        if (dto.description() != null) {
            warranty.setDescription(dto.description());
        }
        if (dto.observations() != null) {
            warranty.setObservations(dto.observations());
        }
        if (dto.technicianId() != null) {
            warranty.setTechnicianId(dto.technicianId());
        }
        
        warranty.setUpdatedAt(LocalDateTime.now());
        Warranty updated = warrantyRepository.save(warranty);
        return mapToDTO(updated);
    }
    
    @Override
    public WarrantyDTO attendWarranty(String id, String technicianId, String observations) {
        Warranty warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));
        
        if (warranty.getStatus() == WarrantyStatus.CLOSED) {
            throw new RuntimeException("No se puede atender una garantía cerrada");
        }
        
        warranty.setStatus(WarrantyStatus.ATTENDED);
        warranty.setTechnicianId(technicianId);
        if (observations != null) {
            warranty.setObservations(observations);
        }
        warranty.setUpdatedAt(LocalDateTime.now());
        
        Warranty updated = warrantyRepository.save(warranty);
        return mapToDTO(updated);
    }
    
    @Override
    public WarrantyDTO closeWarranty(String id, String observations) {
        Warranty warranty = warrantyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));
        
        warranty.setStatus(WarrantyStatus.CLOSED);
        if (observations != null) {
            warranty.setObservations(observations);
        }
        warranty.setUpdatedAt(LocalDateTime.now());
        
        Warranty updated = warrantyRepository.save(warranty);
        return mapToDTO(updated);
    }
    
    @Override
    public void checkAndCloseExpiredWarranties() {
        List<Warranty> expiredWarranties = warrantyRepository
            .findByEndDateBeforeAndStatusNot(LocalDateTime.now(), WarrantyStatus.CLOSED);
        
        expiredWarranties.forEach(warranty -> {
            warranty.setStatus(WarrantyStatus.CLOSED);
            warranty.setUpdatedAt(LocalDateTime.now());
            warrantyRepository.save(warranty);
        });
    }
    
    private WarrantyDTO mapToDTO(Warranty warranty) {
        return new WarrantyDTO(
            warranty.getId(),
            warranty.getServiceOrderId(),
            warranty.getClientId(),
            warranty.getVehicleId(),
            warranty.getStatus(),
            warranty.getStartDate(),
            warranty.getEndDate(),
            warranty.getCreatedAt(),
            warranty.getUpdatedAt(),
            warranty.getDescription(),
            warranty.getObservations(),
            warranty.getTechnicianId()
        );
    }
}

