package com.egrocery_back.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "egrocery", catalog = "")
public class OrdersEntity {
    private Integer id;
    private BigDecimal totalPrice;
    private Timestamp createdAt;
    private Collection<OrderItemsEntity> orderItemsById;
    private Collection<OrderStatusEntity> orderStatusesById;
    private UsersEntity usersByUserId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "total_price", nullable = false, precision = 2)
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Basic
    @Column(name = "created_at", nullable = true)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersEntity that = (OrdersEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usersByUserId, totalPrice, orderStatusesById, createdAt);
    }

    @OneToMany(mappedBy = "ordersByOrderId")
    public Collection<OrderItemsEntity> getOrderItemsById() {
        return orderItemsById;
    }

    public void setOrderItemsById(Collection<OrderItemsEntity> orderItemsById) {
        this.orderItemsById = orderItemsById;
    }

    @OneToMany(mappedBy = "ordersByOrderId")
    public Collection<OrderStatusEntity> getOrderStatusesById() {
        return orderStatusesById;
    }

    public void setOrderStatusesById(Collection<OrderStatusEntity> orderStatusesById) {
        this.orderStatusesById = orderStatusesById;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public UsersEntity getUsersByUserId() {
        return usersByUserId;
    }

    public void setUsersByUserId(UsersEntity usersByUserId) {
        this.usersByUserId = usersByUserId;
    }
}
