package com.egrocery_back.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class CartFilterDTO {

    @Min(value = 1, message = "O ID deve ser maior ou igual a 1")
    private Integer id;

    @Min(value = 1, message = "A quantidade deve ser maior ou igual a 1")
    private Integer quantity;

    @Size(max = 100, message = "O nome do usuário deve ter no máximo 100 caracteres")
    private String userName;

    @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres")
    private String productName;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
