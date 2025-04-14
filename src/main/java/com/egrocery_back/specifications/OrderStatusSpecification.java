package com.egrocery_back.specifications;

import com.egrocery_back.dto.OrderStatusFilterDTO;
import com.egrocery_back.models.OrderStatusEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusSpecification {
    public static Specification<OrderStatusEntity> filter(OrderStatusFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            
            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                predicates.add(cb.between(root.get("updatedAt"), 
                    filter.getStartDate(), 
                    filter.getEndDate()));
            }
            
            if (filter.getOrderId() != null) {
                predicates.add(cb.equal(root.get("ordersByOrderId").get("id"), filter.getOrderId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
