package com.egrocery_back.dto;

import com.egrocery_back.models.OrderStatusEntity;
import java.sql.Timestamp;

public class OrderStatusFilterDTO {
    private OrderStatusEntity.Status status;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer orderId;

    // Construtor
    public OrderStatusFilterDTO() {}

    // GETTER para orderId
    public Integer getOrderId() {
        return this.orderId;
    }

    // SETTER para orderId
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    // Outros getters/setters (manter os existentes)
    public OrderStatusEntity.Status getStatus() {
        return status;
    }
    
    public Timestamp getStartDate() {
        return startDate;
    }
    
    public Timestamp getEndDate() {
        return endDate;
    }
}
