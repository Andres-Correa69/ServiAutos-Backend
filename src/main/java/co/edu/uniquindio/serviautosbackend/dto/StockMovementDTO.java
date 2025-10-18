package co.edu.uniquindio.serviautosbackend.dto;

import java.time.LocalDateTime;

public record StockMovementDTO(
        String id,
        String sparePartId,
        String movementType, // ENTRY, EXIT, ADJUSTMENT
        Integer quantity,
        String reason,
        LocalDateTime date,
        String userId
) {}
