package com.egrocery_back.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.egrocery_back.dto.OrderDTO;
import com.egrocery_back.dto.OrderFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.OrderService;

public class OrderControllerTest {

	@InjectMocks
	private OrderController orderController;

	@Mock
	private OrderService orderService;

	private OrderDTO orderDTO;
	private OrderFilterDTO orderFilterDTO;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		orderDTO = Mockito.mock(OrderDTO.class);
		orderFilterDTO = new OrderFilterDTO();
		pageable = PageRequest.of(0, 10);
	}

	@Test
	void getAllOrders_NoFilter_ReturnsOkWithOrderPage() {
		List<OrderDTO> ordersList = Collections.singletonList(orderDTO);
		Page<OrderDTO> orderPage = new PageImpl<>(ordersList, pageable, ordersList.size());
		when(orderService.getAllOrders(any(OrderFilterDTO.class), any(Pageable.class))).thenReturn(orderPage);

		ResponseEntity<Page<OrderDTO>> response = orderController.getAllOrders(orderFilterDTO, pageable);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(orderPage, response.getBody());
		verify(orderService, times(1)).getAllOrders(orderFilterDTO, pageable);
	}

	@Test
	void getOrderById_ValidId_ReturnsOkWithOrderDTO() throws NotFound {
		int orderId = 1;
		when(orderService.getOrderById(orderId)).thenReturn(orderDTO);

		ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(orderDTO, response.getBody());
		verify(orderService, times(1)).getOrderById(orderId);
	}

	@Test
	void getOrderById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int orderId = 1;
		when(orderService.getOrderById(orderId)).thenThrow(new NotFound("Pedido não encontrado", 1));

		assertThrows(NotFound.class, () -> orderController.getOrderById(orderId));
		verify(orderService, times(1)).getOrderById(orderId);
	}

	@Test
    void createOrder_ValidInput_ReturnsOkWithCreatedOrderDTO() throws NotFound {
        when(orderService.saveOrder(orderDTO)).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.createOrder(orderDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderService, times(1)).saveOrder(orderDTO);
    }

	@Test
    void createOrder_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        when(orderService.saveOrder(orderDTO)).thenThrow(new NotFound("Erro ao salvar o pedido",1));

        assertThrows(NotFound.class, () -> orderController.createOrder(orderDTO));
        verify(orderService, times(1)).saveOrder(orderDTO);
    }

	@Test
	void updateOrder_ValidInput_ReturnsOkWithUpdatedOrderDTO() throws NotFound {
		int orderId = 1;
		OrderDTO updatedOrderDTO = new OrderDTO();
		updatedOrderDTO.setId(orderId);
		updatedOrderDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
		when(orderService.updateOrder(orderId, orderDTO)).thenReturn(updatedOrderDTO);

		ResponseEntity<OrderDTO> response = orderController.updateOrder(orderId, orderDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedOrderDTO, response.getBody());
		verify(orderService, times(1)).updateOrder(orderId, orderDTO);
	}

	@Test
	void updateOrder_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int orderId = 1;
		when(orderService.updateOrder(orderId, orderDTO)).thenThrow(new NotFound("Pedido não encontrado", 1));

		assertThrows(NotFound.class, () -> orderController.updateOrder(orderId, orderDTO));
		verify(orderService, times(1)).updateOrder(orderId, orderDTO);
	}

	@Test
	void deleteOrder_ValidId_ReturnsNoContent() throws NotFound {
		int orderId = 1;
		doNothing().when(orderService).deleteOrder(orderId);

		ResponseEntity<Void> response = orderController.deleteOrder(orderId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(orderService, times(1)).deleteOrder(orderId);
	}

	@Test
	void deleteOrder_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int orderId = 1;
		doThrow(new NotFound("Pedido não encontrado", 1)).when(orderService).deleteOrder(orderId);

		assertThrows(NotFound.class, () -> orderController.deleteOrder(orderId));
		verify(orderService, times(1)).deleteOrder(orderId);
	}
}