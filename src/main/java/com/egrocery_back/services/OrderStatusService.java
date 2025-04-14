package com.egrocery_back.services;

import com.egrocery_back.dto.OrderStatusDTO;
import com.egrocery_back.dto.OrderStatusFilterDTO;
import com.egrocery_back.models.OrderStatusEntity;
import com.egrocery_back.repositories.OrderStatusRepository;
import com.egrocery_back.specifications.OrderStatusSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderStatusService {

    @Autowired
    private OrderStatusRepository repository;

    @Transactional
    public OrderStatusDTO create(OrderStatusDTO dto) {
        if (repository.existsByStatusAndOrdersByOrderId_Id(dto.getStatus(), dto.getOrder().getId())) {
            throw new IllegalArgumentException("Este pedido já possui o status " + dto.getStatus());
        }

        OrderStatusEntity entity = convertToEntity(dto);
        entity = repository.save(entity);
        return convertToDTO(entity);
    }

    public List<OrderStatusDTO> filter(OrderStatusFilterDTO filter) {
        Specification<OrderStatusEntity> spec = OrderStatusSpecification.filter(filter);
        return repository.findAll(spec).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public OrderStatusDTO getById(Integer id) {
        OrderStatusEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Status não encontrado com o ID: " + id));
        return convertToDTO(entity);
    }

    @Transactional
    public OrderStatusDTO update(Integer id, OrderStatusDTO dto) {
        OrderStatusEntity entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Status não encontrado com o ID: " + id));
        
        // Validação para evitar duplicação
        if (!entity.getStatus().equals(dto.getStatus()) && 
            repository.existsByStatusAndOrdersByOrderId_Id(dto.getStatus(), dto.getOrder().getId())) {
            throw new IllegalArgumentException("Este pedido já possui o status " + dto.getStatus());
        }

        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // Atualize outros campos conforme necessário
        
        repository.save(entity);
        return convertToDTO(entity);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Status não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    // Métodos auxiliares de conversão
    private OrderStatusEntity convertToEntity(OrderStatusDTO dto) {
        OrderStatusEntity entity = new OrderStatusEntity();
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(dto.getUpdatedAt());
        // Obs.: Carregar a OrdersEntity associada (implementação depende do seu contexto)
        return entity;
    }

    private OrderStatusDTO convertToDTO(OrderStatusEntity entity) {
        OrderStatusDTO dto = new OrderStatusDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
