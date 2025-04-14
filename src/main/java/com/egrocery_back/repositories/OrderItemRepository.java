package com.egrocery_back.repositories;

import com.egrocery_back.models.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderItemRepository extends JpaRepository<OrderItemsEntity, Integer>, JpaSpecificationExecutor<OrderItemsEntity> {
}
