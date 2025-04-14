package com.egrocery_back.specifications;

import com.egrocery_back.dto.OfferFilterDTO;
import com.egrocery_back.models.OffersEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OfferSpecification {

    public static Specification<OffersEntity> filter(OfferFilterDTO filter) {
        return (Root<OffersEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), filter.getStartDate().toLocalDate().atStartOfDay()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), filter.getEndDate().toLocalDate().atTime(23, 59, 59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
