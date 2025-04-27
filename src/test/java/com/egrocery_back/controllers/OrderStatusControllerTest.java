package com.egrocery_back.controllers;

import com.egrocery_back.dto.OrderStatusDTO;
import com.egrocery_back.dto.OrderStatusFilterDTO;
import com.egrocery_back.models.OrderStatusEntity;
import com.egrocery_back.services.OrderStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderStatusControllerTest {

    @InjectMocks
    private OrderStatusController orderStatusController;

    @Mock
    private OrderStatusService orderStatusService;

    private OrderStatusDTO orderStatusDTO;
    private OrderStatusFilterDTO orderStatusFilterDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderStatusDTO = new OrderStatusDTO();
        orderStatusDTO.setId(1);
        orderStatusDTO.setStatus(OrderStatusEntity.Status.PENDING);
        orderStatusFilterDTO = new OrderStatusFilterDTO();
    }

    @Test
    void createOrderStatus_ValidInput_ReturnsCreatedOrderStatusDTO() {
        when(orderStatusService.create(orderStatusDTO)).thenReturn(orderStatusDTO);

        ResponseEntity<OrderStatusDTO> response = orderStatusController.createOrderStatus(orderStatusDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderStatusDTO, response.getBody());
        verify(orderStatusService, times(1)).create(orderStatusDTO);
    }

    @Test
    void getAvailableStatusTypes_ReturnsListOfStatusTypes() {
        ResponseEntity<List<String>> response = orderStatusController.getAvailableStatusTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> expectedTypes = Arrays.stream(OrderStatusEntity.Status.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        assertEquals(expectedTypes, response.getBody());
    }

    @Test
    void getAllOrderStatuses_NoFilter_ReturnsListOfOrderStatusDTO() {
        List<OrderStatusDTO> statusesList = Collections.singletonList(orderStatusDTO);
        when(orderStatusService.filter(orderStatusFilterDTO)).thenReturn(statusesList);

        ResponseEntity<List<OrderStatusDTO>> response = orderStatusController.getAllOrderStatuses(orderStatusFilterDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statusesList, response.getBody());
        verify(orderStatusService, times(1)).filter(orderStatusFilterDTO);
    }

    @Test
    void getOrderStatusById_ValidId_ReturnsOrderStatusDTO() {
        int statusId = 1;
        when(orderStatusService.getById(statusId)).thenReturn(orderStatusDTO);

        ResponseEntity<OrderStatusDTO> response = orderStatusController.getOrderStatusById(statusId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderStatusDTO, response.getBody());
        verify(orderStatusService, times(1)).getById(statusId);
    }

    @Test
    void getOrderStatusById_NotFound_ReturnsNull() {
        int statusId = 1;
        when(orderStatusService.getById(statusId)).thenReturn(null);

        ResponseEntity<OrderStatusDTO> response = orderStatusController.getOrderStatusById(statusId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(orderStatusService, times(1)).getById(statusId);
    }

    @Test
    void updateOrderStatus_ValidInput_ReturnsUpdatedOrderStatusDTO() {
        int statusId = 1;
        OrderStatusDTO updatedStatusDTO = new OrderStatusDTO();
        updatedStatusDTO.setId(statusId);
        updatedStatusDTO.setStatus(OrderStatusEntity.Status.SHIPPED);
        when(orderStatusService.update(statusId, orderStatusDTO)).thenReturn(updatedStatusDTO);

        ResponseEntity<OrderStatusDTO> response = orderStatusController.updateOrderStatus(statusId, orderStatusDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStatusDTO, response.getBody());
        verify(orderStatusService, times(1)).update(statusId, orderStatusDTO);
    }

    @Test
    void updateOrderStatus_NotFound_ReturnsNull() {
        int statusId = 1;
        when(orderStatusService.update(statusId, orderStatusDTO)).thenReturn(null);

        ResponseEntity<OrderStatusDTO> response = orderStatusController.updateOrderStatus(statusId, orderStatusDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(orderStatusService, times(1)).update(statusId, orderStatusDTO);
    }

    @Test
    void deleteOrderStatus_ValidId_ReturnsNoContent() {
        int statusId = 1;
        doNothing().when(orderStatusService).delete(statusId);

        ResponseEntity<Void> response = orderStatusController.deleteOrderStatus(statusId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderStatusService, times(1)).delete(statusId);
    }
}