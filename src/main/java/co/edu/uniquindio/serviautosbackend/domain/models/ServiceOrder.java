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
    private String laborValue;
    private LocalDateTime dateService;
    private List<SparePartDetail> spareParts;
    private Status status;




}
