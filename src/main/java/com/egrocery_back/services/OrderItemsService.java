package com.egrocery_back.services;

import com.egrocery_back.dto.OrderItemDTO;
import com.egrocery_back.dto.OrderItemsFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemsService {

    // Cria um item de pedido
    OrderItemDTO createOrderItem(OrderItemDTO orderItemsDTO);
    
    // Atualiza um item de pedido
    OrderItemDTO updateOrderItem(Integer id, OrderItemDTO orderItemsDTO) throws NotFound;
    
    // Filtra os itens de pedido com base nos parâmetros fornecidos
    Page<OrderItemDTO> filterOrderItems(OrderItemsFilterDTO filter, Pageable pageable);
    
    // Obtém um item de pedido pelo ID
    OrderItemDTO getOrderItemById(Integer id) throws NotFound;
    
    // Exclui um item de pedido pelo ID
    void deleteOrderItem(Integer id) throws NotFound;
}
