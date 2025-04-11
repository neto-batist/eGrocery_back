package com.egrocery_back.specifications;

import com.egrocery_back.models.ProductsEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<ProductsEntity> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ProductsEntity> categoryEquals(String category) {
        return (root, query, cb) ->
                category == null || category.isEmpty()
                        ? null
                        : cb.equal(root.get("category"), category);
    }

    public static Specification<ProductsEntity> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<ProductsEntity> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
