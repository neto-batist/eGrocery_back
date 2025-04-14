package com.egrocery_back.specifications;

import com.egrocery_back.dto.OrderFilterDTO;
import com.egrocery_back.dto.OrderStatusDTO;
import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.models.OrdersEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSpecification {

    public static Specification<OrdersEntity> filter(OrderFilterDTO filter) {
        return (Root<OrdersEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtrar por usuários (IDs extraídos dos DTOs)
            if (filter.getUsers() != null && !filter.getUsers().isEmpty()) {
                List<Integer> userIds = filter.getUsers().stream()
                        .map(UserDTO::getId)
                        .collect(Collectors.toList());
                predicates.add(root.get("user").get("id").in(userIds));
            }

            // Filtrar por status (IDs extraídos dos DTOs)
            if (filter.getOrderStatus() != null && !filter.getOrderStatus().isEmpty()) {
                List<Integer> statusIds = filter.getOrderStatus().stream()
                        .map(OrderStatusDTO::getId)
                        .collect(Collectors.toList());
                predicates.add(root.get("status").get("id").in(statusIds));
            }

            // Data de início
            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate().atStartOfDay()));
            }

            // Data de fim
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate().atTime(23, 59, 59)));
            }

            // Total mínimo
            if (filter.getMinTotal() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("total"), filter.getMinTotal()));
            }

            // Total máximo
            if (filter.getMaxTotal() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("total"), filter.getMaxTotal()));
            }

            // IDs dos pedidos
            if (filter.getOrderIds() != null && !filter.getOrderIds().isEmpty()) {
                predicates.add(root.get("id").in(filter.getOrderIds()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
