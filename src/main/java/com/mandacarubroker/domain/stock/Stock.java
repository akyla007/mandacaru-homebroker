package com.mandacarubroker.domain.stock;

import com.mandacarubroker.domain.dto.RequestStockDTO;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;


@Entity(name="stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")

public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String symbol;
    private String companyName;
    private double price;
    private double variation;
    private static final DecimalFormat decForm = new DecimalFormat("#.####");

    public Stock(RequestStockDTO requestStockDTO) {
        this.symbol = requestStockDTO.symbol();
        this.companyName = requestStockDTO.companyName();
        this.price = changePrice(requestStockDTO.price());

    }

    public double changePrice(double amount) {
        if (this.price != 0) {
            String number = decForm.format(((amount - this.price) / this.price) * 100);
            this.variation = Double.parseDouble(number.replace(",", "."));
        }
        return amount;
    }

}