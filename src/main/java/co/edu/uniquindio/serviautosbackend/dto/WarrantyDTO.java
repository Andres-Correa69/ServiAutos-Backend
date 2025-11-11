package co.edu.uniquindio.serviautosbackend.dto;

import co.edu.uniquindio.serviautosbackend.domain.models.WarrantyStatus;
import java.time.LocalDateTime;

public record WarrantyDTO(
    String id,
    String serviceOrderId,
    String clientId,
    String vehicleId,
    WarrantyStatus status,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String description,
    String observations,
    String technicianId
) {}

