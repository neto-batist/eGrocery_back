package com.egrocery_back.repositories;

import com.egrocery_back.models.OrderItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItemsEntity, Integer>,
        JpaSpecificationExecutor<OrderItemsEntity> {

    List<OrderItemsEntity> findByIdIn(List<Integer> ids);
}
