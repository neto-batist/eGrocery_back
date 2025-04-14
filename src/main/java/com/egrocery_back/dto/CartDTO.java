package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CartDTO {

    @JsonProperty("id")
    private Integer id;
    
    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @NotNull(message = "User is required.")
    @JsonProperty("user")
    private UserDTO user;

    @NotNull(message = "Product is required.")
    @JsonProperty("product")
    private ProductDTO product;
}
