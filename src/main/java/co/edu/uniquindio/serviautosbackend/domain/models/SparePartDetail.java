package co.edu.uniquindio.serviautosbackend.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SparePartDetail {

    private String idSparePart;
    private String name;
    private Integer amount;
    private Double price;

    public SparePartDetail(String idSparePart, String name, Integer amount, Double price) {
        this.idSparePart = idSparePart;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public String getIdSparePart() {
        return idSparePart;
    }

    public void setIdSparePart(String idSparePart) {
        this.idSparePart = idSparePart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
