package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class OrderItemDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("order")
    @NotNull(message = "O pedido é obrigatório")
    private OrderDTO order;

    @JsonProperty("product")
    @NotNull(message = "O produto é obrigatório")
    private ProductDTO product;

    @JsonProperty("quantity")
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer quantity;

    @JsonProperty("price")
    @NotNull(message = "O preço é obrigatório")
    private Double price;

    public void setId(Integer id) {
        this.id = id;
    }
}
