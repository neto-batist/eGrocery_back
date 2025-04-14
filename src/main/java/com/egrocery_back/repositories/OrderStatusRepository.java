package com.egrocery_back.repositories;

import com.egrocery_back.models.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatusEntity, Integer>, JpaSpecificationExecutor<OrderStatusEntity> {
    boolean existsByStatus(OrderStatusEntity.Status status);
    Optional<OrderStatusEntity> findByStatus(OrderStatusEntity.Status status);
}
