package com.egrocery_back.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.egrocery_back.dto.CartDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.CartService;

public class CartControllerTest {

	@InjectMocks
	private CartController cartController;

	@Mock
	private CartService cartService;

	private CartDTO cartDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		cartDTO = Mockito.mock(CartDTO.class);
	}

	@Test
	void addItemToCartSucess() throws NotFound {
		Mockito.when(cartService.addItemToCart(any())).thenReturn(cartDTO);
		ResponseEntity<CartDTO> retorno = cartController.addItemToCart(cartDTO);
		assertEquals(cartDTO, retorno.getBody());
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		verify(cartService, times(1)).addItemToCart(cartDTO);
	}

	@Test
	void removeItemFromCartSucess() throws NotFound {
		Mockito.doNothing().when(cartService).removeItemFromCart(any());
		ResponseEntity<Void> retorno = cartController.removeItemFromCart(1);
		assertTrue(retorno.getStatusCode().is2xxSuccessful());
		;
		assertEquals(HttpStatus.NO_CONTENT, retorno.getStatusCode());
		verify(cartService, times(1)).removeItemFromCart(1);
	}

	@Test
	void updateCartItemSucess() throws NotFound {
		Mockito.when(cartService.updateItemInCart(anyInt(), any())).thenReturn(cartDTO);
		ResponseEntity<CartDTO> retorno = cartController.updateCartItem(1, cartDTO);
		assertEquals(cartDTO, retorno.getBody());
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		verify(cartService, times(1)).updateItemInCart(1, cartDTO);
	}

	@Test
	void calculateTotalSucess() throws NotFound {
		double retornoEsperado = new Random().nextDouble();
		Mockito.when(cartService.calculateTotal(any())).thenReturn(retornoEsperado);
		ResponseEntity<Double> retorno = cartController.calculateTotal(1);
		assertEquals(retornoEsperado, retorno.getBody());
	}

	@Test
	void removeItemFromCart_NotFound() throws NotFound {
		Mockito.doThrow(new NotFound("ItemFromCart", cartDTO.getId())).when(cartService).removeItemFromCart(any());
		assertThrows(NotFound.class, () -> cartController.removeItemFromCart(1));
		verify(cartService, times(1)).removeItemFromCart(1);
	}

	@Test
	void updateCartItem_NotFound() throws NotFound {
		Mockito.doThrow(new NotFound("CartItem", cartDTO.getId())).when(cartService).updateItemInCart(anyInt(), any());
		assertThrows(NotFound.class, () -> cartController.updateCartItem(1, cartDTO));
		verify(cartService, times(1)).updateItemInCart(1, cartDTO);
	}

	@Test
	void calculateTotal_NotFound() throws NotFound {
		Mockito.doThrow(new NotFound("CartItem", cartDTO.getId())).when(cartService).calculateTotal(any());
		assertThrows(NotFound.class, () -> cartController.calculateTotal(1));
	}

}