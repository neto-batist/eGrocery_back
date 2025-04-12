package com.egrocery_back.dto;

import jakarta.validation.constraints.Size;

public class CategoryFilterDTO {

    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
