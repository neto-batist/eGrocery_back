package com.egrocery_back.config;

import com.egrocery_back.models.OrderStatusEntity;
import com.egrocery_back.models.OrderStatusEntity.Status;
import com.egrocery_back.repositories.OrderStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class OrderStatusInitializer implements CommandLineRunner {

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusInitializer(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public void run(String... args) {
        for (Status status : Status.values()) {
            if (!orderStatusRepository.existsByStatus(status)) {
                OrderStatusEntity entity = new OrderStatusEntity();
                entity.setStatus(status);
                entity.setUpdatedAt(Timestamp.from(Instant.now()));
                // setar um order
                orderStatusRepository.save(entity);
            }
        }
    }
}
