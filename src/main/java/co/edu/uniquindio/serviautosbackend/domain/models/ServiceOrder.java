package co.edu.uniquindio.serviautosbackend.domain.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document("serviceOrders")
@Getter
@Setter
@NoArgsConstructor

public class ServiceOrder {

    @Id
    private String id;
    private String clientId;
    private String vehicleId;
    private String diagnostic;
    private String assignedTechnician;
    private Double laborValue;
    private LocalDateTime dateService;
    private List<SparePartDetail> spareParts;
    private Status status;

    public ServiceOrder(String id, String clientId, String vehicleId, String diagnostic,
                        String assignedTechnician, Double laborValue, LocalDateTime dateService,
                        List<SparePartDetail> spareParts, Status status) {
        this.id = id;
        this.clientId = clientId;
        this.vehicleId = vehicleId;
        this.diagnostic = diagnostic;
        this.assignedTechnician = assignedTechnician;
        this.laborValue = laborValue;
        this.dateService = dateService;
        this.spareParts = spareParts;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public Double getLaborValue() {
        return laborValue;
    }

    public void setLaborValue(Double laborValue) {
        this.laborValue = laborValue;
    }

    public LocalDateTime getDateService() {
        return dateService;
    }

    public void setDateService(LocalDateTime dateService) {
        this.dateService = dateService;
    }

    public List<SparePartDetail> getSpareParts() {
        return spareParts;
    }

    public void setSpareParts(List<SparePartDetail> spareParts) {
        this.spareParts = spareParts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
