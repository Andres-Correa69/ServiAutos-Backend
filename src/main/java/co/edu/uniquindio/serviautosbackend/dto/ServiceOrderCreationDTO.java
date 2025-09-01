package co.edu.uniquindio.serviautosbackend.dto;

public record ServiceOrderCreationDTO(
        String clientId,
        String vehicleId,
        String diagnostic,
        String assignedTechnician,
        Double laborValue
) {}
