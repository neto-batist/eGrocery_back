package com.egrocery_back.config;

import com.egrocery_back.models.OrdersEntity;
import com.egrocery_back.models.OrderStatusEntity;
import com.egrocery_back.models.OrderStatusEntity.Status;
import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.repositories.OrderRepository;
import com.egrocery_back.repositories.OrderStatusRepository;
import com.egrocery_back.repositories.UsersRepository; // Import corrigido para UsersRepository
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class OrderStatusInitializer implements CommandLineRunner {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository; // Nome corrigido para UsersRepository

    public OrderStatusInitializer(OrderStatusRepository orderStatusRepository,
                                OrderRepository orderRepository,
                                UsersRepository usersRepository) { // Tipo corrigido
        this.orderStatusRepository = orderStatusRepository;
        this.orderRepository = orderRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public void run(String... args) {
        // Verifica se já existe um usuário admin/default
        UsersEntity defaultUser = usersRepository.findById(1)
            .orElseGet(() -> {
                UsersEntity user = new UsersEntity();
                user.setName("Admin Padrão");
                // Configure outros campos obrigatórios do UsersEntity
                return usersRepository.save(user);
            });

        // Cria um pedido padrão se não existir
        OrdersEntity defaultOrder = orderRepository.findById(1)
            .orElseGet(() -> {
                OrdersEntity order = new OrdersEntity();
                order.setTotalPrice(0.0);
                order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                order.setUsersByUserId(defaultUser);
                return orderRepository.save(order);
            });

        // Cria os status iniciais
        for (Status status : Status.values()) {
            if (!orderStatusRepository.existsByStatusAndOrdersByOrderId(status, defaultOrder)) {
                OrderStatusEntity entity = new OrderStatusEntity();
                entity.setStatus(status);
                entity.setUpdatedAt(Timestamp.from(Instant.now()));
                entity.setOrdersByOrderId(defaultOrder);
                orderStatusRepository.save(entity);
            }
        }
    }
}
