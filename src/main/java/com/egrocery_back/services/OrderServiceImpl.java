package com.egrocery_back.services;

import com.egrocery_back.dto.OrderDTO;
import com.egrocery_back.dto.OrderFilterDTO;
import com.egrocery_back.dto.OrderStatusDTO;
import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.*;
import com.egrocery_back.repositories.*;
import com.egrocery_back.specifications.OrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UsersRepository userRepository;
    private final OrderStatusRepository orderStatusRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UsersRepository userRepository,
                            OrderStatusRepository orderStatusRepository,
                            OrderItemRepository orderItemRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public OrderDTO saveOrder(OrderDTO dto) throws NotFound {
        OrdersEntity order = mapToEntity(dto);
        OrdersEntity saved = orderRepository.save(order);
        return mapToDTO(saved);
    }

    @Override
    public Page<OrderDTO> getAllOrders(OrderFilterDTO filter, Pageable pageable) {
        return orderRepository.findAll(OrderSpecification.filter(filter), pageable)
                .map(this::mapToDTO);
    }

    @Override
    public OrderDTO getOrderById(Integer id) throws NotFound {
        OrdersEntity entity = findOrderById(id);
        return mapToDTO(entity);
    }

    @Override
    public OrderDTO updateOrder(Integer id, OrderDTO dto) throws NotFound {
        OrdersEntity existing = findOrderById(id);

        existing =  mapToEntity(dto);
        existing.setId(id);

        OrdersEntity updated = orderRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void deleteOrder(Integer id) throws NotFound {
        OrdersEntity order = findOrderById(id);
        orderRepository.delete(order);
    }

    // ---------- PRIVATE METHODS ----------

    public OrdersEntity mapToEntity(OrderDTO dto) throws NotFound {
        OrdersEntity entity = new OrdersEntity();
        entity.setId(dto.getId());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUsersByUserId(getUserEntity(dto.getUser().getId()));

        if (dto.getStatuses() != null && !dto.getStatuses().isEmpty()) {
            List<OrderStatusEntity> statusEntities = new ArrayList<>();
            for (OrderStatusDTO statusDTO : dto.getStatuses()) {
                OrderStatusEntity statusEntity = getOrderStatusEntity(statusDTO.getId());
                statusEntity.setOrdersByOrderId(entity); // Associa o pedido ao status (bidirecional)
                statusEntities.add(statusEntity);
            }
            entity.setOrderStatuses(statusEntities); // Setar manualmente a coleção
        }

        return entity;
    }

    public OrderDTO mapToDTO(OrdersEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setCreatedAt(entity.getCreatedAt());

        //lembrar de mudar para o map de usuario quando criar
        if (entity.getUsersByUserId() != null) {
            dto.setUser(new UserDTO());
            dto.getUser().setId(entity.getUsersByUserId().getId());
            dto.getUser().setName(entity.getUsersByUserId().getName());
        }

        if (entity.getOrderStatuses() != null && !entity.getOrderStatuses().isEmpty()) {
            List<OrderStatusDTO> statusDTOs = new ArrayList<>();
            for (OrderStatusEntity statusEntity : entity.getOrderStatuses()) {
                statusDTOs.add(mapToStatusDTO(statusEntity));
            }
            dto.setStatuses(statusDTOs);
        }

        return dto;
    }

    private UsersEntity getUserEntity(Integer userId) throws NotFound {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFound("Usuário", userId));
    }

    private OrderStatusEntity getOrderStatusEntity(Integer statusId) throws NotFound {
        if (statusId == null) return null;
        return orderStatusRepository.findById(statusId)
                .orElseThrow(() -> new NotFound("Status", statusId));
    }

    private OrdersEntity findOrderById(Integer id) throws NotFound {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFound("Pedido", id));
    }

    private OrderStatusDTO mapToStatusDTO(OrderStatusEntity entity) {
        OrderStatusDTO dto = new OrderStatusDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
