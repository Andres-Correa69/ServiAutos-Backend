package co.edu.uniquindio.serviautosbackend.dto;

import co.edu.uniquindio.serviautosbackend.domain.models.SparePartDetail;
import co.edu.uniquindio.serviautosbackend.domain.models.Status;

import java.time.LocalDateTime;
import java.util.List;

public record ServiceOrderDTO(
        String id,
        String clientId,
        String vehicleId,
        String diagnostic,
        String assignedTechnicianId,
        Double laborValue,
        LocalDateTime dateService,
        Status status,
        List<SparePartDetail> spareParts  // ✅ AGREGAR ESTA LÍNEA
) {}