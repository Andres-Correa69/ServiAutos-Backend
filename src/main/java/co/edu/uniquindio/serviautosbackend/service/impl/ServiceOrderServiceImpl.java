package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.ServiceOrder;
import co.edu.uniquindio.serviautosbackend.domain.models.SparePart;
import co.edu.uniquindio.serviautosbackend.domain.models.SparePartDetail;
import co.edu.uniquindio.serviautosbackend.domain.models.Status;
import co.edu.uniquindio.serviautosbackend.dto.AddSparePartToOrderDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderDTO;
import co.edu.uniquindio.serviautosbackend.repository.ServiceOrderRepository;
import co.edu.uniquindio.serviautosbackend.repository.SparePartRepository;
import co.edu.uniquindio.serviautosbackend.service.ServiceOrderService;
import co.edu.uniquindio.serviautosbackend.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private SparePartRepository sparePartRepository;

    @Autowired
    private WarrantyService warrantyService;

    @Value("${warranty.default-days:30}")
    private Integer defaultWarrantyDays;

    @Override
    public ServiceOrderDTO createOrder(ServiceOrderCreationDTO dto) {
        ServiceOrder order = new ServiceOrder();
        order.setClientId(dto.clientId());
        order.setVehicleId(dto.vehicleId());
        order.setDiagnostic(dto.diagnostic());
        order.setAssignedTechnicianId(dto.assignedTechnicianId());
        order.setLaborValue(dto.laborValue()); // ⚠️ mejor cambiar laborValue a Double en model
        order.setDateService(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        ServiceOrder saved = serviceOrderRepository.save(order);
        return mapToDTO(saved);
    }

    @Override
    public ServiceOrderDTO getOrderById(String id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return mapToDTO(order);
    }

    @Override
    public List<ServiceOrderDTO> getAllOrders() {
        return serviceOrderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public ServiceOrderDTO updateOrder(String id, ServiceOrderCreationDTO dto) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.setClientId(dto.clientId());
        order.setVehicleId(dto.vehicleId());
        order.setDiagnostic(dto.diagnostic());
        order.setAssignedTechnicianId(dto.assignedTechnicianId());
        order.setLaborValue(dto.laborValue());

        ServiceOrder updated = serviceOrderRepository.save(order);
        return mapToDTO(updated);
    }

    @Override
    public void deleteOrder(String id) {
        serviceOrderRepository.deleteById(id);
    }

    private ServiceOrderDTO mapToDTO(ServiceOrder order) {
        return new ServiceOrderDTO(
                order.getId(),
                order.getClientId(),
                order.getVehicleId(),
                order.getDiagnostic(),
                order.getAssignedTechnicianId(),
                order.getLaborValue(),
                order.getDateService(),
                order.getStatus(),
                order.getSpareParts() != null ? order.getSpareParts() : new ArrayList<>()  // ✅ AGREGAR ESTA LÍNEA
        );
    }



    @Override
    public ServiceOrderDTO attendOrder(String id, String technician) {
        ServiceOrder order = serviceOrderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.setStatus(Status.FINALIZED);
        order.setAssignedTechnicianId(technician); // Ahora recibe el ID del técnico
        order.setDateService(LocalDateTime.now());

        serviceOrderRepository.save(order);

            return new ServiceOrderDTO(
                order.getId(),
            order.getClientId(),
            order.getVehicleId(),
            order.getDiagnostic(),
            order.getAssignedTechnicianId(),
            order.getLaborValue(),
            order.getDateService(),
            order.getStatus(),
            order.getSpareParts() != null ? order.getSpareParts() : new ArrayList<>()  // ✅ AGREGAR ESTA LÍNEA
        );
    }

    @Override
    public ServiceOrderDTO addSparePartToOrder(String orderId, AddSparePartToOrderDTO dto) {
        // Validar que el DTO no sea null
        if (dto == null || dto.sparePartId() == null || dto.quantity() == null) {
            throw new RuntimeException("Datos del repuesto inválidos");
        }
    
        // Validar cantidad positiva
        if (dto.quantity() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
    
        ServiceOrder order = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    
        if (order.getStatus() == Status.FINALIZED) {
            throw new RuntimeException("No se pueden agregar repuestos a una orden finalizada");
        }
    
        SparePart sparePart = sparePartRepository.findById(dto.sparePartId())
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
    
        if (sparePart.getAvailableStock() < dto.quantity()) {
            throw new RuntimeException("Stock insuficiente del repuesto. Disponible: " + sparePart.getAvailableStock() + ", Solicitado: " + dto.quantity());
        }
    
        // ✅ ACTUALIZAR: Crear detalle del repuesto con los nombres correctos
        SparePartDetail detail = new SparePartDetail(
                dto.sparePartId(),      // idSparePart
                sparePart.getName(),    // name
                dto.quantity(),         // amount
                sparePart.getUnitValue() // price
        );
    
        // Agregar a la lista de repuestos de la orden
        if (order.getSpareParts() == null) {
            order.setSpareParts(new java.util.ArrayList<>());
        }
        order.getSpareParts().add(detail);
    
        ServiceOrder updated = serviceOrderRepository.save(order);
        return mapToDTO(updated);
    }
    @Override
    public ServiceOrderDTO removeSparePartFromOrder(String orderId, String sparePartId) {
        ServiceOrder order = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (order.getStatus() == Status.FINALIZED) {
            throw new RuntimeException("No se pueden remover repuestos de una orden finalizada");
        }

        if (order.getSpareParts() != null) {
            order.getSpareParts().removeIf(detail -> detail.getIdSparePart().equals(sparePartId));
        }

        ServiceOrder updated = serviceOrderRepository.save(order);
        return mapToDTO(updated);
    }

    @Override
    public ServiceOrderDTO finalizeOrder(String orderId) {
        ServiceOrder order = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (order.getStatus() == Status.FINALIZED) {
            throw new RuntimeException("La orden ya está finalizada");
        }

        // Reducir stock de los repuestos utilizados
        if (order.getSpareParts() != null) {
            for (SparePartDetail detail : order.getSpareParts()) {
                SparePart sparePart = sparePartRepository.findById(detail.getIdSparePart())
                        .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
                
                sparePart.setAvailableStock(sparePart.getAvailableStock() - detail.getAmount());
                sparePartRepository.save(sparePart);
            }
        }

        order.setStatus(Status.FINALIZED);
        order.setDateService(LocalDateTime.now());

        ServiceOrder updated = serviceOrderRepository.save(order);
        
        // ✅ NUEVO: Crear garantía automáticamente al finalizar la orden
        try {
            warrantyService.createWarranty(
                updated.getId(),
                updated.getClientId(),
                updated.getVehicleId(),
                defaultWarrantyDays,
                "Garantía creada automáticamente al finalizar la orden de servicio"
            );
        } catch (Exception e) {
            // Log del error pero no fallar la finalización de la orden
            System.err.println("Error al crear garantía: " + e.getMessage());
        }
        
        return mapToDTO(updated);
    }

}