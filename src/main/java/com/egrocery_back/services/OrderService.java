package com.egrocery_back.services;

import com.egrocery_back.dto.OrderDTO;
import com.egrocery_back.dto.OrderFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDTO saveOrder(OrderDTO dto) throws NotFound;

    Page<OrderDTO> getAllOrders(OrderFilterDTO filter, Pageable pageable);

    OrderDTO getOrderById(Integer id) throws NotFound;

    OrderDTO updateOrder(Integer id, OrderDTO dto) throws NotFound;

    void deleteOrder(Integer id) throws NotFound;
}
