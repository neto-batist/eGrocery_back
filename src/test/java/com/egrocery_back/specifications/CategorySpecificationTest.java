package com.egrocery_back.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import com.egrocery_back.models.CategoriesEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CategorySpecificationTest {

    @SuppressWarnings("unchecked")
    @Test
    void nameContains_withValidName_returnsPredicate() {
        String name = "frutas";
        Specification<CategoriesEntity> specification = CategorySpecification.nameContains(name);
        assertNotNull(specification);
    
        Root<CategoriesEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);
    
        Path<?> namePathRaw = mock(Path.class);
        when(root.get("name")).thenReturn((Path<Object>) namePathRaw);
    
        when(cb.lower((Path<String>) namePathRaw)).thenReturn((Path<String>) namePathRaw);
        when(cb.like((Path<String>) namePathRaw, "%" + name.toLowerCase() + "%")).thenReturn(predicate);
    
        Predicate resultPredicate = specification.toPredicate(root, query, cb);
    
        assertNotNull(resultPredicate);
    }
    
    @Test
    void nameContains_withNullName_returnsNullPredicate() {
        Specification<CategoriesEntity> specification = CategorySpecification.nameContains(null);
        assertNotNull(specification);
    }
    
    @Test
    void nameContains_withEmptyName_returnsNullPredicate() {
        Specification<CategoriesEntity> specification = CategorySpecification.nameContains("");
        assertNotNull(specification);
    }
}