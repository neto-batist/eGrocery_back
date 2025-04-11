package com.egrocery_back.repositories;

import com.egrocery_back.models.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoriesEntity, Integer>,
        JpaSpecificationExecutor<CategoriesEntity> {
}
