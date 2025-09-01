package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.ServiceOrderDTO;

import java.util.List;

public interface ServiceOrderService {

    ServiceOrderDTO createOrder(ServiceOrderCreationDTO dto);

    ServiceOrderDTO getOrderById(String id);

    List<ServiceOrderDTO> getAllOrders();

    ServiceOrderDTO updateOrder(String id, ServiceOrderCreationDTO dto);

    void deleteOrder(String id);
}

