package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonProperty("user_id")
    private UserDTO user;

    @NotNull(message = "Product is required.")
    @JsonProperty("product")
    private ProductDTO product;
}
