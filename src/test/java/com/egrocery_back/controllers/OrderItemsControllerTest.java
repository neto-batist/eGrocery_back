package com.egrocery_back.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.egrocery_back.dto.OrderItemDTO;
import com.egrocery_back.dto.OrderItemsFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.OrderItemsService;

public class OrderItemsControllerTest {

    @InjectMocks
    private OrderItemsController orderItemsController;

    @Mock
    private OrderItemsService orderItemsService;

    private OrderItemDTO orderItemDTO;
    private OrderItemsFilterDTO orderItemsFilterDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemDTO = Mockito.mock(OrderItemDTO.class);
        orderItemsFilterDTO = new OrderItemsFilterDTO();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void create_ValidInput_ReturnsCreatedOrderItemDTO() {
        when(orderItemsService.createOrderItem(orderItemDTO)).thenReturn(orderItemDTO);

        OrderItemDTO createdOrderItem = orderItemsController.create(orderItemDTO);

        assertEquals(orderItemDTO, createdOrderItem);
        verify(orderItemsService, times(1)).createOrderItem(orderItemDTO);
    }

    @Test
    void update_ValidInput_ReturnsUpdatedOrderItemDTO() throws NotFound {
        int orderItemId = 1;
        OrderItemDTO updatedOrderItemDTO = new OrderItemDTO();
        updatedOrderItemDTO.setId(orderItemId);
        updatedOrderItemDTO.setQuantity(3);
        when(orderItemsService.updateOrderItem(orderItemId, orderItemDTO)).thenReturn(updatedOrderItemDTO);

        OrderItemDTO updatedOrderItem = orderItemsController.update(orderItemId, orderItemDTO);

        assertEquals(updatedOrderItemDTO, updatedOrderItem);
        verify(orderItemsService, times(1)).updateOrderItem(orderItemId, orderItemDTO);
    }

    @Test
    void update_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        int orderItemId = 1;
        when(orderItemsService.updateOrderItem(orderItemId, orderItemDTO)).thenThrow(new NotFound("Item do pedido não encontrado",1));

        assertThrows(NotFound.class, () -> orderItemsController.update(orderItemId, orderItemDTO));
        verify(orderItemsService, times(1)).updateOrderItem(orderItemId, orderItemDTO);
    }

    @Test
    void getById_ValidId_ReturnsOrderItemDTO() throws NotFound {
        int orderItemId = 1;
        when(orderItemsService.getOrderItemById(orderItemId)).thenReturn(orderItemDTO);

        OrderItemDTO foundOrderItem = orderItemsController.getById(orderItemId);

        assertEquals(orderItemDTO, foundOrderItem);
        verify(orderItemsService, times(1)).getOrderItemById(orderItemId);
    }

    @Test
    void getById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        int orderItemId = 1;
        when(orderItemsService.getOrderItemById(orderItemId)).thenThrow(new NotFound("Item do pedido não encontrado",1));

        assertThrows(NotFound.class, () -> orderItemsController.getById(orderItemId));
        verify(orderItemsService, times(1)).getOrderItemById(orderItemId);
    }

    @Test
    void delete_ValidId_CallsDeleteServiceMethod() throws NotFound {
        int orderItemId = 1;
        doNothing().when(orderItemsService).deleteOrderItem(orderItemId);

        orderItemsController.delete(orderItemId);

        verify(orderItemsService, times(1)).deleteOrderItem(orderItemId);
    }

    @Test
    void delete_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        int orderItemId = 1;
        doThrow(new NotFound("Item do pedido não encontrado",1)).when(orderItemsService).deleteOrderItem(orderItemId);

        assertThrows(NotFound.class, () -> orderItemsController.delete(orderItemId));
        verify(orderItemsService, times(1)).deleteOrderItem(orderItemId);
    }

    @Test
    void filter_ValidFilterAndPageable_ReturnsOrderItemPage() {
        List<OrderItemDTO> orderItemsList = Collections.singletonList(orderItemDTO);
        Page<OrderItemDTO> orderItemPage = new PageImpl<>(orderItemsList, pageable, orderItemsList.size());
        when(orderItemsService.filterOrderItems(orderItemsFilterDTO, pageable)).thenReturn(orderItemPage);

        Page<OrderItemDTO> resultPage = orderItemsController.filter(orderItemsFilterDTO, pageable);

        assertEquals(orderItemPage, resultPage);
        verify(orderItemsService, times(1)).filterOrderItems(orderItemsFilterDTO, pageable);
    }
}