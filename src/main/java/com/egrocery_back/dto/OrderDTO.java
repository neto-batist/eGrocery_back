package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Data
public class OrderDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("user")
    @NotNull(message = "O usuário do pedido é obrigatório")
    private UserDTO user;

    @JsonProperty("status")
    private OrderStatusDTO status;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("order_items")
    private List<OrderItemDTO> orderItems;
}
