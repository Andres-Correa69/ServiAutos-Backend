package co.edu.uniquindio.serviautosbackend.domain.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("spareParts")
@Getter
@Setter
@NoArgsConstructor
public class SparePart {

    @Id
    private String id;
    private String name;
    private String detail;
    private String category;
    private String brand;
    private String partNumber;
    private Double unitValue;
    private Integer availableStock;
    private Integer minimumStock;
    private String location;

    public SparePart(String id, String name, String detail, String category, String brand,
                     String partNumber, Double unitValue, Integer availableStock, 
                     Integer minimumStock, String location) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.category = category;
        this.brand = brand;
        this.partNumber = partNumber;
        this.unitValue = unitValue;
        this.availableStock = availableStock;
        this.minimumStock = minimumStock;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Double unitValue) {
        this.unitValue = unitValue;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(Integer minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
