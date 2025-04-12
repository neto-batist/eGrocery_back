package com.egrocery_back.dto;

import com.egrocery_back.models.OrderStatusEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Data
public class OrderStatusDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("status")
    private OrderStatusEntity.Status status;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    @JsonProperty("order")
    @NotNull
    @NotBlank(message = "O pedido deve ser obrigatório")
    private OrderDTO order;

}
