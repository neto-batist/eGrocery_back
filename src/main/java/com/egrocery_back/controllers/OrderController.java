package com.egrocery_back.controllers;

import com.egrocery_back.dto.OrderDTO;
import com.egrocery_back.dto.OrderFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @Valid OrderFilterDTO filter,
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdAt") // ordenação padrão por data de criação
            }) Pageable pageable
    ) {
        Page<OrderDTO> orders = orderService.getAllOrders(filter, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable @Min(1) Integer id) throws NotFound {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO) throws NotFound {
        OrderDTO createdOrder = orderService.saveOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable @Min(1) Integer id,
            @RequestBody @Valid OrderDTO orderDTO) throws NotFound {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable @Min(1) Integer id) throws NotFound {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
