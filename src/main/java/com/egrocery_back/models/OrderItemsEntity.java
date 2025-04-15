package com.egrocery_back.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items", schema = "egrocery", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsEntity {
    private Integer id;
    private Integer quantity;
    private Double price;
    private OrdersEntity ordersByOrderId;
    private ProductsEntity productsByProductId;
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        }
    }

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
    @Column(name = "quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "price", nullable = false, precision = 2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "created_at", nullable = false)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    public OrdersEntity getOrdersByOrderId() {
        return ordersByOrderId;
    }

    public void setOrdersByOrderId(OrdersEntity ordersByOrderId) {
        this.ordersByOrderId = ordersByOrderId;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    public ProductsEntity getProductsByProductId() {
        return productsByProductId;
    }

    public void setProductsByProductId(ProductsEntity productsByProductId) {
        this.productsByProductId = productsByProductId;
    }

    // Construtor alternativo (sem ID)
    public OrderItemsEntity(Integer quantity, Double price, OrdersEntity ordersByOrderId, ProductsEntity productsByProductId) {
        this.quantity = quantity;
        this.price = price;
        this.ordersByOrderId = ordersByOrderId;
        this.productsByProductId = productsByProductId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemsEntity that = (OrderItemsEntity) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(quantity, that.quantity) &&
               Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ordersByOrderId, productsByProductId, quantity, price);
    }
}
