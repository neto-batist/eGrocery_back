package com.egrocery_back.specifications;

import com.egrocery_back.dto.FaqsFilterDTO;
import com.egrocery_back.models.FaqsEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FaqsSpecification {

    public static Specification<FaqsEntity> filter(FaqsFilterDTO filter) {
        return (Root<FaqsEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por 'question'
            if (filter.getQuestion() != null && !filter.getQuestion().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("question")), "%" + filter.getQuestion().toLowerCase() + "%"));
            }

            // Filtro por 'isFrequentlyAsked'
            if (filter.getIsFrequentlyAsked() != null) {
                predicates.add(cb.equal(root.get("isFrequentlyAsked"), filter.getIsFrequentlyAsked()));
            }

            // Retorna a combinação dos predicados com 'AND'
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
