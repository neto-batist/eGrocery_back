package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class OrderItemsFilterDTO {

    @JsonProperty("orderId")
    private Integer orderId;

    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("minQuantity")
    @Min(value = 1, message = "A quantidade mínima não pode ser menor que 1")
    private Integer minQuantity;

    @JsonProperty("maxQuantity")
    @Min(value = 1, message = "A quantidade máxima não pode ser menor que 1")
    private Integer maxQuantity;

    @JsonProperty("minPrice")
    @DecimalMin(value = "0.0", inclusive = true, message = "O preço não pode ser negativo")
    private Double minPrice;

    @JsonProperty("maxPrice")
    @DecimalMin(value = "0.0", inclusive = true, message = "O preço não pode ser negativo")
    private Double maxPrice;

    // Validação para garantir que minQuantity <= maxQuantity, caso ambos sejam fornecidos
    @AssertTrue(message = "A quantidade mínima não pode ser maior que a quantidade máxima")
    public boolean isValidQuantityRange() {
        if (minQuantity != null && maxQuantity != null) {
            return minQuantity <= maxQuantity;
        }
        return true; // válido se um dos dois for null
    }

    // Validação para garantir que minPrice <= maxPrice, caso ambos sejam fornecidos
    @AssertTrue(message = "O preço mínimo não pode ser maior que o preço máximo")
    public boolean isValidPriceRange() {
        if (minPrice != null && maxPrice != null) {
            return minPrice <= maxPrice;
        }
        return true; // válido se um dos dois for null
    }
}