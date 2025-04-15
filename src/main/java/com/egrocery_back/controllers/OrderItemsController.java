package com.egrocery_back.controllers;

import com.egrocery_back.dto.OrderItemDTO;
import com.egrocery_back.dto.OrderItemsFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.OrderItemsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-items")
public class OrderItemsController {

    @Autowired
    private OrderItemsService orderItemsService;

    @PostMapping("/create")
    public OrderItemDTO create(@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemsService.createOrderItem(orderItemDTO);
    }

    @PutMapping("/update/{id}")
    public OrderItemDTO update(@PathVariable Integer id, @RequestBody OrderItemDTO orderItemDTO) throws NotFound {
        return orderItemsService.updateOrderItem(id, orderItemDTO);
    }

    @GetMapping("/search/{id}")
    public OrderItemDTO getById(@PathVariable Integer id) throws NotFound {
        return orderItemsService.getOrderItemById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) throws NotFound {
        orderItemsService.deleteOrderItem(id);
    }

    @PostMapping("/filter")
    public Page<OrderItemDTO> filter(@RequestBody OrderItemsFilterDTO filter, Pageable pageable) {
        return orderItemsService.filterOrderItems(filter, pageable);
    }

}
