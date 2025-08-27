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
    private Double unitValue;
    private Integer availableStock;

    public SparePart(String id, String name, String detail,
                     Double unitValue, Integer availableStock) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.unitValue = unitValue;
        this.availableStock = availableStock;
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
}
