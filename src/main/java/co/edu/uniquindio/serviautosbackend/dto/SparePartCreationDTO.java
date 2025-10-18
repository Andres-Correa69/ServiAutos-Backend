package co.edu.uniquindio.serviautosbackend.dto;

public record SparePartCreationDTO(
        String name,
        String detail,
        String category,
        String brand,
        String partNumber,
        Double unitValue,
        Integer availableStock,
        Integer minimumStock,
        String location
) {}
