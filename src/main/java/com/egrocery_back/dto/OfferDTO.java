package com.egrocery_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("discount_percentage")
    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0")
    private Double discountPercentage;

    @JsonProperty("start_date")
    @NotNull(message = "Start date is required")
    private Timestamp startDate;

    @JsonProperty("end_date")
    @NotNull(message = "End date is required")
    private Timestamp endDate;

    @JsonProperty("product")
    @NotNull(message = "Product is required")
    private ProductDTO product;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
