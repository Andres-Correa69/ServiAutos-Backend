package co.edu.uniquindio.serviautosbackend.dto;


public record VehicleDTO(
        String id,
        String licencePlate,
        String brand,
        String model,
        String clientId
) {}