package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ProductDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres")
    private String name;

    @JsonProperty("description")
    @Size(max = 1000, message = "A descrição do produto deve ter no máximo 1000 caracteres")
    private String description;

    @JsonProperty("price")
    @NotNull(message = "O preço do produto é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private BigDecimal price;

    @JsonProperty("stock_quantity")
    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
    private Integer stockQuantity;

    @JsonProperty("image_url")
    @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres")
    private String imageUrl;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("category_id")
    @NotNull(message = "O ID da categoria é obrigatório")
    private Integer categoryId;

    @JsonProperty("cart_ids")
    private List<Integer> cartIds;

    @JsonProperty("offer_ids")
    private List<Integer> offerIds;

    @JsonProperty("order_item_ids")
    private List<Integer> orderItemIds;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getCartIds() {
        return cartIds;
    }

    public void setCartIds(List<Integer> cartIds) {
        this.cartIds = cartIds;
    }

    public List<Integer> getOfferIds() {
        return offerIds;
    }

    public void setOfferIds(List<Integer> offerIds) {
        this.offerIds = offerIds;
    }

    public List<Integer> getOrderItemIds() {
        return orderItemIds;
    }

    public void setOrderItemIds(List<Integer> orderItemIds) {
        this.orderItemIds = orderItemIds;
    }
}
