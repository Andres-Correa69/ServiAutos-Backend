package co.edu.uniquindio.serviautosbackend.dto;

import co.edu.uniquindio.serviautosbackend.domain.models.WarrantyStatus;

public record WarrantyUpdateDTO(
    WarrantyStatus status,
    String description,
    String observations,
    String technicianId
) {}

