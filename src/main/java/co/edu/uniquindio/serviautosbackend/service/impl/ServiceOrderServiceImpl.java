package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.ServiceOrder;
import co.edu.uniquindio.serviautosbackend.domain.models.Status;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderDTO;
import co.edu.uniquindio.serviautosbackend.repository.ServiceOrderRepository;
import co.edu.uniquindio.serviautosbackend.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Override
    public ServiceOrderDTO createOrder(ServiceOrderCreationDTO dto) {
        ServiceOrder order = new ServiceOrder();
        order.setClientId(dto.clientId());
        order.setVehicleId(dto.vehicleId());
        order.setDiagnostic(dto.diagnostic());
        order.setAssignedTechnician(dto.assignedTechnician());
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
        order.setAssignedTechnician(dto.assignedTechnician());
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
                order.getAssignedTechnician(),
                order.getLaborValue(),
                order.getDateService(),
                order.getStatus()
        );
    }
}