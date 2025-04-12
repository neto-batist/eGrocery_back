package com.egrocery_back.specifications;

import com.egrocery_back.models.CategoriesEntity;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<CategoriesEntity> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isEmpty()
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
