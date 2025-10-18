package co.edu.uniquindio.serviautosbackend.domain.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("stockMovements")
@Getter
@Setter
@NoArgsConstructor
public class StockMovement {

    @Id
    private String id;
    private String sparePartId;
    private String movementType; // ENTRY, EXIT, ADJUSTMENT
    private Integer quantity;
    private String reason;
    private LocalDateTime date;
    private String userId;

    public StockMovement(String id, String sparePartId, String movementType, 
                        Integer quantity, String reason, LocalDateTime date, String userId) {
        this.id = id;
        this.sparePartId = sparePartId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
        this.date = date;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSparePartId() {
        return sparePartId;
    }

    public void setSparePartId(String sparePartId) {
        this.sparePartId = sparePartId;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
