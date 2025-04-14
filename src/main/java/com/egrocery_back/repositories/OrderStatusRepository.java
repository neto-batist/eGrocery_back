package com.egrocery_back.repositories;

import com.egrocery_back.models.OrderStatusEntity;
import com.egrocery_back.models.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;

public interface OrderStatusRepository extends 
    JpaRepository<OrderStatusEntity, Integer>, 
    JpaSpecificationExecutor<OrderStatusEntity> {

    boolean existsByStatus(OrderStatusEntity.Status status);
    Optional<OrderStatusEntity> findByStatus(OrderStatusEntity.Status status);
    
    // Métodos para buscar por OrderId (ID)
    boolean existsByStatusAndOrdersByOrderId_Id(OrderStatusEntity.Status status, Integer orderId);
    
    // Método para buscar pela entidade Order completa
    boolean existsByStatusAndOrdersByOrderId(OrderStatusEntity.Status status, OrdersEntity order);
    
    @EntityGraph(attributePaths = {"ordersByOrderId"})
    Optional<OrderStatusEntity> findWithOrderById(Integer id);
}
