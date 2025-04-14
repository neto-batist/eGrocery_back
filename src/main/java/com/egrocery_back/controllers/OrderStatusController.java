package com.egrocery_back.controllers;

import com.egrocery_back.dto.OrderStatusDTO;
import com.egrocery_back.dto.OrderStatusFilterDTO;
import com.egrocery_back.services.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-status")
public class OrderStatusController {

    @Autowired
    private OrderStatusService orderStatusService;

    @PostMapping
    public ResponseEntity<OrderStatusDTO> createOrderStatus(@RequestBody OrderStatusDTO orderStatusDTO) {
        OrderStatusDTO createdStatus = orderStatusService.create(orderStatusDTO);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }
    @GetMapping("/status-types")
    public ResponseEntity<List<String>> getAvailableStatusTypes() {
        List<String> statusTypes = Arrays.stream(OrderStatusEntity.Status.values())
            .map(Enum::name)
            .collect(Collectors.toList());
        return ResponseEntity.ok(statusTypes);
    }

    // Obter todos os status com filtros opcionais
    @GetMapping
    public ResponseEntity<List<OrderStatusDTO>> getAllOrderStatuses(OrderStatusFilterDTO filter) {
        List<OrderStatusDTO> statuses = orderStatusService.filter(filter);
        return ResponseEntity.ok(statuses);
    }

    // Obter um status específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusDTO> getOrderStatusById(@PathVariable Integer id) {
        OrderStatusDTO status = orderStatusService.getById(id);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusDTO> updateOrderStatus(
            @PathVariable Integer id, 
            @RequestBody OrderStatusDTO orderStatusDTO) {
        OrderStatusDTO updatedStatus = orderStatusService.update(id, orderStatusDTO);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Integer id) {
        orderStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
