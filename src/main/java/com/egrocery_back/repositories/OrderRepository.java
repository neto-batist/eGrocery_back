package com.egrocery_back.repositories;

import com.egrocery_back.models.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<OrdersEntity, Integer>, JpaSpecificationExecutor<OrdersEntity> {

}

