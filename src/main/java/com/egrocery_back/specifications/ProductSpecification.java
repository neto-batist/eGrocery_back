package com.egrocery_back.specifications;

import com.egrocery_back.models.CategoriesEntity;
import com.egrocery_back.models.ProductsEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<ProductsEntity> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ProductsEntity> categoryEquals(Integer categoryId) {
        return (Root<ProductsEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            // Join com a entidade Categories para comparar o id da categoria
            Join<ProductsEntity, CategoriesEntity> categoryJoin = root.join("categoriesByCategoryId");
            return cb.equal(categoryJoin.get("id"), categoryId);
        };
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
