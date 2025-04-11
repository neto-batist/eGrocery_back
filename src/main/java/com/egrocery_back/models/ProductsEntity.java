package com.egrocery_back.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "products", schema = "egrocery", catalog = "")
@Data
public class ProductsEntity {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private Timestamp createdAt;
    private Collection<CartEntity> cartsById;
    private Collection<OffersEntity> offersById;
    private Collection<OrderItemsEntity> orderItemsById;
    private CategoriesEntity categoriesByCategoryId;

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
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "price", nullable = false, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "stock_quantity", nullable = false)
    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


    @Basic
    @Column(name = "image_url", nullable = true, length = 255)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        ProductsEntity that = (ProductsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(stockQuantity, that.stockQuantity) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, stockQuantity, categoriesByCategoryId, imageUrl, createdAt);
    }

    @OneToMany(mappedBy = "productsByProductId")
    public Collection<CartEntity> getCartsById() {
        return cartsById;
    }

    public void setCartsById(Collection<CartEntity> cartsById) {
        this.cartsById = cartsById;
    }

    @OneToMany(mappedBy = "productsByProductId")
    public Collection<OffersEntity> getOffersById() {
        return offersById;
    }

    public void setOffersById(Collection<OffersEntity> offersById) {
        this.offersById = offersById;
    }

    @OneToMany(mappedBy = "productsByProductId")
    public Collection<OrderItemsEntity> getOrderItemsById() {
        return orderItemsById;
    }

    public void setOrderItemsById(Collection<OrderItemsEntity> orderItemsById) {
        this.orderItemsById = orderItemsById;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    public CategoriesEntity getCategoriesByCategoryId() {
        return categoriesByCategoryId;
    }

    public void setCategoriesByCategoryId(CategoriesEntity categoriesByCategoryId) {
        this.categoriesByCategoryId = categoriesByCategoryId;
    }
}
