package co.edu.uniquindio.serviautosbackend.domain.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    private String id;
    private String licencePlate;
    private String brand;
    private String model;
    private String clientId;

    public Vehicle(String id, String licencePlate, String brand, String model, String clientId) {
        this.id = id;
        this.licencePlate = licencePlate;
        this.brand = brand;
        this.model = model;
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
