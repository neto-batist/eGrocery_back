package com.egrocery_back.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import com.egrocery_back.dto.CartFilterDTO;
import com.egrocery_back.models.CartEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CartSpecificationTest {

	@Test
	void filter_withMultipleCriteria_returnsCombinedSpecification() {
		CartFilterDTO filter = new CartFilterDTO();
		filter.setId(1);
		filter.setQuantity(2);
		filter.setUserName("João");
		filter.setProductName("Maçã");
		Specification<CartEntity> specification = CartSpecification.filter(filter);
		assertNotNull(specification);

		Root<CartEntity> root = mock(Root.class);
		CriteriaQuery<?> query = mock(CriteriaQuery.class);
		CriteriaBuilder cb = mock(CriteriaBuilder.class);
		Predicate predicate1 = mock(Predicate.class);
		Predicate predicate2 = mock(Predicate.class);
		Predicate predicate3 = mock(Predicate.class);
		Predicate predicate4 = mock(Predicate.class);
		Path<Object> userJoin = mock(jakarta.persistence.criteria.Join.class);
		Path<Object> productJoin = mock(jakarta.persistence.criteria.Join.class);

		when(root.get("id")).thenReturn(Mockito.mock(jakarta.persistence.criteria.Path.class));
		when(root.get("quantity")).thenReturn(Mockito.mock(jakarta.persistence.criteria.Path.class));
		when(root.get("user")).thenReturn(userJoin);
		when(userJoin.get("name")).thenReturn(Mockito.mock(jakarta.persistence.criteria.Path.class));
		when(root.get("product")).thenReturn(productJoin);
		when(productJoin.get("name")).thenReturn(Mockito.mock(jakarta.persistence.criteria.Path.class));

		when(cb.equal(root.get("id"), filter.getId())).thenReturn(predicate1);
		when(cb.equal(root.get("quantity"), filter.getQuantity())).thenReturn(predicate2);
		when(cb.like(root.get("user").get("name"), "%" + filter.getUserName() + "%")).thenReturn(predicate3);
		when(cb.like(root.get("product").get("name"), "%" + filter.getProductName() + "%")).thenReturn(predicate4);
		when(cb.and(predicate1, predicate2, predicate3, predicate4)).thenReturn(Mockito.mock(Predicate.class));

		Predicate resultPredicate = specification.toPredicate(root, query, cb);
		assertNotNull(resultPredicate);
	}

}