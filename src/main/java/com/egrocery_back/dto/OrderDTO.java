package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Collection;
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

    @JsonProperty("statuses")
    private Collection<OrderStatusDTO> statuses;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("order_items")
    private List<OrderItemDTO> orderItems;

    @JsonProperty("total_price")
    private Double totalPrice;

    // Construtor padrão
    public OrderDTO() {
    }

    // Construtor que recebe apenas o id
    public OrderDTO(Integer id) {
        this.id = id;
    }

    // Construtores, getters e setters restantes

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Collection<OrderStatusDTO> getStatuses() {
        return statuses;
    }

    public void setStatuses(Collection<OrderStatusDTO> statuses) {
        this.statuses = statuses;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
