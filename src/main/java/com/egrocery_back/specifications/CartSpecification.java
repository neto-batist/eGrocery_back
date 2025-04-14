package com.egrocery_back.specifications;

import com.egrocery_back.dto.CartFilterDTO;
import com.egrocery_back.models.CartEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CartSpecification {

    public static Specification<CartEntity> filter(CartFilterDTO filter) {
        return (Root<CartEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtra por ID do carrinho
            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            // Filtra por quantidade
            if (filter.getQuantity() != null) {
                predicates.add(cb.equal(root.get("quantity"), filter.getQuantity()));
            }

            // Filtra por nome do usuário (realizando join com a tabela de usuários)
            if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
                predicates.add(cb.like(root.get("user").get("name"), "%" + filter.getUserName() + "%"));
            }

            // Filtra por nome do produto (realizando join com a tabela de produtos)
            if (filter.getProductName() != null && !filter.getProductName().isEmpty()) {
                predicates.add(cb.like(root.get("product").get("name"), "%" + filter.getProductName() + "%"));
            }

            // Combinando todas as condições
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
