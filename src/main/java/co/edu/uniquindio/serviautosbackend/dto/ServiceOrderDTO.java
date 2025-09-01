package co.edu.uniquindio.serviautosbackend.dto;

import co.edu.uniquindio.serviautosbackend.domain.models.Status;

import java.time.LocalDateTime;

public record ServiceOrderDTO(
        String id,
        String clientId,
        String vehicleId,
        String diagnostic,
        String assignedTechnician,
        Double laborValue,
        LocalDateTime dateService,
        Status status
) {}