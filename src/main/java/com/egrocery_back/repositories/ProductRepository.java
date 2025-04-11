package com.egrocery_back.repositories;

import com.egrocery_back.models.ProductsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductsEntity, Integer>,
        JpaSpecificationExecutor<ProductsEntity> {
    Page<ProductsEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
