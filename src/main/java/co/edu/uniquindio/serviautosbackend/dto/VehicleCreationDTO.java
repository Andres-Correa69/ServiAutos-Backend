package co.edu.uniquindio.serviautosbackend.dto;


public record VehicleCreationDTO(
        String licencePlate,
        String brand,
        String model,
        String clientId
) {}