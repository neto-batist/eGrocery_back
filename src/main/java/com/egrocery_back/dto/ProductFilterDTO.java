package com.egrocery_back.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class ProductFilterDTO {

    private String name;

    private String category;

    @DecimalMin(value = "0.0", inclusive = true, message = "Preço mínimo não pode ser negativo")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", inclusive = true, message = "Preço máximo não pode ser negativo")
    private BigDecimal maxPrice;

    @jakarta.validation.constraints.AssertTrue(message = "O preço mínimo não pode ser maior que o preço máximo")
    public boolean isValidPriceRange() {
        if (minPrice != null && maxPrice != null) {
            return minPrice.compareTo(maxPrice) <= 0;
        }
        return true; // válido se um dos dois for null
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}
