package com.egrocery_back.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.dto.OrderDTO;
import com.egrocery_back.dto.OrderItemDTO;
import com.egrocery_back.dto.OrderItemsFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.*;
import com.egrocery_back.repositories.*;
import com.egrocery_back.specifications.OrderItemsSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OfferRepository offersRepository;

    // Cria um item de pedido
    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        // Busca o produto diretamente do banco, com o createdAt já preenchido
        ProductsEntity productsEntity = productRepository.findById(orderItemDTO.getProduct().getId())
            .orElseThrow(() -> new EntityNotFoundException("Produto com id " + orderItemDTO.getProduct().getId() + " não encontrado"));
    
        // Busca o pedido do banco
        OrdersEntity ordersEntity = orderRepository.findById(orderItemDTO.getOrder().getId())
            .orElseThrow(() -> new EntityNotFoundException("Pedido com id " + orderItemDTO.getOrder().getId() + " não encontrado"));
    
        // Cria o item de pedido
        OrderItemsEntity orderItemsEntity = new OrderItemsEntity(
            orderItemDTO.getQuantity(),
            orderItemDTO.getPrice(),
            ordersEntity,
            productsEntity
        );
    
        // Salva no banco
        orderItemsRepository.save(orderItemsEntity);
    
        // Atribui ID e createdAt ao DTO (caso o DTO possua esse campo)
        orderItemDTO.setId(orderItemsEntity.getId());
        if (orderItemsEntity.getCreatedAt() != null) {
            orderItemDTO.setCreatedAt(orderItemsEntity.getCreatedAt().toLocalDateTime());
        }
    
        // Atualiza os dados do produto retornado (para garantir consistência, incluindo createdAt)
        orderItemDTO.setProduct(convertProductToDTO(productsEntity));
    
        return orderItemDTO;
    }
    
    // Utilitário para converter ProductsEntity em ProductDTO
    private ProductDTO convertProductToDTO(ProductsEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCategoryId(entity.getCategoriesByCategoryId().getId());
        return dto;
    }

    // Atualiza um item de pedido
    @Override
    public OrderItemDTO updateOrderItem(Integer id, OrderItemDTO orderItemDTO) throws NotFound {
        // Buscar o item de pedido pelo id
        OrderItemsEntity orderItemsEntity = orderItemsRepository.findById(id)
                .orElseThrow(() -> new NotFound("OrderItem", id));

        // Atualizar os campos quantity e price
        orderItemsEntity.setQuantity(orderItemDTO.getQuantity());
        orderItemsEntity.setPrice(orderItemDTO.getPrice());

        // Salvar a entidade no banco
        orderItemsRepository.saveAndFlush(orderItemsEntity);

        // Atualiza o DTO com os dados atualizados da entidade
        orderItemDTO.setId(orderItemsEntity.getId());
        orderItemDTO.setQuantity(orderItemsEntity.getQuantity());
        orderItemDTO.setPrice(orderItemsEntity.getPrice());

        // Preenche o produto com todos os dados a partir do banco
        ProductsEntity productsEntity = productRepository.findById(orderItemsEntity.getProductsByProductId().getId())
                .orElseThrow(() -> new EntityNotFoundException("Produto com id " + orderItemsEntity.getProductsByProductId().getId() + " não encontrado"));

        orderItemDTO.setProduct(convertProductToDTO(productsEntity));
        orderItemDTO.setOrder(new OrderDTO(orderItemsEntity.getOrdersByOrderId().getId()));

        // Preenche createdAt
        if (orderItemsEntity.getCreatedAt() != null) {
            orderItemDTO.setCreatedAt(orderItemsEntity.getCreatedAt().toLocalDateTime());
        }

        return orderItemDTO;
    }

    // Filtra os itens de pedido com base nos parâmetros fornecidos
    @Override
    public Page<OrderItemDTO> filterOrderItems(OrderItemsFilterDTO filter, Pageable pageable) {
        return orderItemsRepository.findAll(
                OrderItemsSpecification.orderIdEquals(filter.getOrderId())
                        .and(OrderItemsSpecification.productIdEquals(filter.getProductId()))
                        .and(OrderItemsSpecification.quantityGreaterThanOrEqual(filter.getMinQuantity()))
                        .and(OrderItemsSpecification.quantityLessThanOrEqual(filter.getMaxQuantity()))
                        .and(OrderItemsSpecification.priceGreaterThanOrEqual(filter.getMinPrice()))
                        .and(OrderItemsSpecification.priceLessThanOrEqual(filter.getMaxPrice())),
                pageable
        ).map(this::convertToDTO);
    }

    @Override
    public OrderItemDTO getOrderItemById(Integer id) throws NotFound {
        OrderItemsEntity orderItemsEntity = orderItemsRepository.findById(id)
                .orElseThrow(() -> new NotFound("Item de pedido não encontrado com id", id));
        return convertToDTO(orderItemsEntity);
    }

    @Override
    public void deleteOrderItem(Integer id) throws NotFound {
        OrderItemsEntity orderItemsEntity = orderItemsRepository.findById(id)
                .orElseThrow(() -> new NotFound("Item de pedido não encontrado com id", id));
        orderItemsRepository.delete(orderItemsEntity);
    }

    private OrderItemDTO convertToDTO(OrderItemsEntity orderItemsEntity) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItemsEntity.getId());
        orderItemDTO.setQuantity(orderItemsEntity.getQuantity());
        orderItemDTO.setPrice(orderItemsEntity.getPrice());

        // Preenchendo os dados de produto no DTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(orderItemsEntity.getProductsByProductId().getId());
        productDTO.setName(orderItemsEntity.getProductsByProductId().getName());
        productDTO.setDescription(orderItemsEntity.getProductsByProductId().getDescription());
        productDTO.setPrice(orderItemsEntity.getProductsByProductId().getPrice());
        productDTO.setStockQuantity(orderItemsEntity.getProductsByProductId().getStockQuantity());
        productDTO.setImageUrl(orderItemsEntity.getProductsByProductId().getImageUrl());
        productDTO.setCreatedAt(orderItemsEntity.getProductsByProductId().getCreatedAt());
        productDTO.setCategoryId(orderItemsEntity.getProductsByProductId().getCategoriesByCategoryId().getId());
        
        // Preenchendo os dados de pedido no DTO
        orderItemDTO.setOrder(new OrderDTO(orderItemsEntity.getOrdersByOrderId().getId()));   
           
        orderItemDTO.setProduct(productDTO);
        return orderItemDTO;
    }

    // Métodos auxiliares atualizados

    private Collection<CartEntity> obterCartEntityPorIds(List<Integer> cartIds) {
        return cartIds == null || cartIds.isEmpty() ? List.of() : cartRepository.findByIdIn(cartIds);
    }

    private Collection<OffersEntity> obterOfferEntityPorIds(List<Integer> offerIds) {
        return offerIds == null || offerIds.isEmpty() ? List.of() : offersRepository.findByIdIn(offerIds);
    }

    private Collection<OrderItemsEntity> obterOrderItensEntityPorIds(List<Integer> orderItemIds) {
        return orderItemIds == null || orderItemIds.isEmpty() ? List.of() : orderItemsRepository.findByIdIn(orderItemIds);
    }

    private CategoriesEntity obterCategoryPorId(Integer categoryId) {
        if (categoryId == null) {
            throw new EntityNotFoundException("Categoria com id nulo.");
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria com id " + categoryId + " não encontrada"));
    }
}
