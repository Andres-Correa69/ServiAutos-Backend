package co.edu.uniquindio.serviautosbackend.domain.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("warranties")
@Getter
@Setter
@NoArgsConstructor
@Data
public class Warranty {
    
    @Id
    private String id;
    private String serviceOrderId;      // Relación con la orden de servicio
    private String clientId;            // Para consultas rápidas
    private String vehicleId;           // Para consultas rápidas
    private WarrantyStatus status;      // OPEN, ATTENDED, CLOSED
    private LocalDateTime startDate;    // Fecha de inicio (cuando se finaliza la orden)
    private LocalDateTime endDate;      // Fecha de vencimiento
    private LocalDateTime createdAt;    // Fecha de creación
    private LocalDateTime updatedAt;    // Última actualización
    private String description;         // Descripción del problema/garantía
    private String observations;        // Observaciones adicionales
    private String technicianId;        // Técnico que atiende la garantía (opcional)
    
    // Constructor
    public Warranty(String id, String serviceOrderId, String clientId, String vehicleId,
                   WarrantyStatus status, LocalDateTime startDate, LocalDateTime endDate,
                   LocalDateTime createdAt, LocalDateTime updatedAt, String description,
                   String observations, String technicianId) {
        this.id = id;
        this.serviceOrderId = serviceOrderId;
        this.clientId = clientId;
        this.vehicleId = vehicleId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.observations = observations;
        this.technicianId = technicianId;
    }
}

