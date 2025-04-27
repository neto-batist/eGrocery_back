package com.egrocery_back.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.egrocery_back.dto.CartDTO;
import com.egrocery_back.dto.CartFilterDTO;
import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.CartEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.repositories.CartRepository;
import com.egrocery_back.repositories.ProductRepository;
import com.egrocery_back.repositories.UsersRepository;

public class CartServiceTest {

	@InjectMocks
	private CartServiceImpl cartService;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UsersRepository userRepository;

	private CartDTO cartDTO;
	private CartEntity cartEntity;
	private ProductsEntity productEntity;
	private UsersEntity userEntity;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		userEntity = new UsersEntity();
		userEntity.setId(1);

		productEntity = new ProductsEntity();
		productEntity.setId(10);
		productEntity.setName("Test Product");
		productEntity.setPrice(BigDecimal.TEN);
		productEntity.setStockQuantity(100);

		cartDTO = new CartDTO();
		cartDTO.setId(1);
		cartDTO.setQuantity(2);
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(productEntity.getId());
		cartDTO.setProduct(productDTO);
		UserDTO user = new UserDTO();
		user.setId(1);
		cartDTO.setUser(user);

		cartEntity = new CartEntity();
		cartEntity.setId(cartDTO.getId());
		cartEntity.setQuantity(cartDTO.getQuantity());
		cartEntity.setProductsByProductId(productEntity);
		cartEntity.setUsersByUserId(userEntity);
		cartEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
	}

	@Test
    void mapToEntity_ValidDTO_ReturnsCorrectEntity() {
        when(productRepository.findById(cartDTO.getProduct().getId())).thenReturn(Optional.of(productEntity));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        CartEntity mappedEntity = cartService.mapToEntity(cartDTO);

        assertEquals(cartDTO.getId(), mappedEntity.getId());
        assertEquals(cartDTO.getQuantity(), mappedEntity.getQuantity());
        assertEquals(cartDTO.getProduct().getId(), mappedEntity.getProductsByProductId().getId());
        assertEquals(cartDTO.getId(), mappedEntity.getUsersByUserId().getId());
        assertNotNull(mappedEntity.getCreatedAt());
    }

	@Test
	void mapToEntity_DTOWithExistingCreatedAt_UsesExistingCreatedAt() {
		Timestamp existingCreatedAt = new Timestamp(System.currentTimeMillis() - 1000);
		cartDTO.setCreatedAt(existingCreatedAt);
		when(productRepository.findById(cartDTO.getProduct().getId())).thenReturn(Optional.of(productEntity));
		when(userRepository.findById(cartDTO.getId())).thenReturn(Optional.of(userEntity));

		CartEntity mappedEntity = cartService.mapToEntity(cartDTO);

		assertEquals(existingCreatedAt, mappedEntity.getCreatedAt());
	}

	@Test
    void mapToEntity_ProductNotFound_ThrowsRuntimeException() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.mapToEntity(cartDTO));
    }

	@Test
	void mapToDTO_ValidEntity_ReturnsCorrectDTO() {
		CartDTO mappedDTO = cartService.mapToDTO(cartEntity);

		assertEquals(cartEntity.getId(), mappedDTO.getId());
		assertEquals(cartEntity.getQuantity(), mappedDTO.getQuantity());
		assertEquals(cartEntity.getCreatedAt(), mappedDTO.getCreatedAt());
		assertEquals(cartEntity.getProductsByProductId().getId(), mappedDTO.getProduct().getId());
		assertEquals(cartEntity.getProductsByProductId().getName(), mappedDTO.getProduct().getName());
		assertEquals(cartEntity.getProductsByProductId().getPrice(), mappedDTO.getProduct().getPrice());
		assertEquals(cartEntity.getProductsByProductId().getStockQuantity(), mappedDTO.getProduct().getStockQuantity());
	}

	@Test
    void addItemToCart_ValidDTO_ReturnsSavedDTO() {
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);
        when(productRepository.findById(cartDTO.getProduct().getId())).thenReturn(Optional.of(productEntity));
        when(userRepository.findById(cartDTO.getId())).thenReturn(Optional.of(userEntity));

        CartDTO savedDTO = cartService.addItemToCart(cartDTO);

        assertNotNull(savedDTO);
        assertEquals(cartDTO.getId(), savedDTO.getId());
        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

	@Test
	void removeItemFromCart_ExistingCartId_DeletesCart() throws NotFound {
		int cartId = 1;
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));

		cartService.removeItemFromCart(cartId);

		verify(cartRepository, times(1)).delete(cartEntity);
	}

	@Test
	void removeItemFromCart_NonExistingCartId_ThrowsNotFound() {
		int cartId = 99;
		when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

		assertThrows(NotFound.class, () -> cartService.removeItemFromCart(cartId));
		verify(cartRepository, never()).delete(any(CartEntity.class));
	}

	@Test
	void calculateTotal_ExistingCartId_ReturnsCorrectTotal() throws NotFound {
		int cartId = 1;
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));

		double total = cartService.calculateTotal(cartId);

		assertEquals(BigDecimal.TEN.multiply(new BigDecimal(cartEntity.getQuantity())).doubleValue(), total);
	}

	@Test
	void calculateTotal_NonExistingCartId_ThrowsNotFound() {
		int cartId = 99;
		when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

		assertThrows(NotFound.class, () -> cartService.calculateTotal(cartId));
	}

	@Test
	void calculateTotal_ProductWithoutPrice_ThrowsRuntimeException() {
		cartEntity.getProductsByProductId().setPrice(null);
		int cartId = 1;
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));

		assertThrows(RuntimeException.class, () -> cartService.calculateTotal(cartId));
	}

	@Test
	void updateItemInCart_ExistingCartId_ReturnsUpdatedDTO() throws NotFound {
		int cartId = 1;
		CartDTO updateDTO = new CartDTO();
		updateDTO.setQuantity(3);
		ProductDTO newProductDTO = new ProductDTO();
		newProductDTO.setId(20);
		updateDTO.setProduct(newProductDTO);

		ProductsEntity newProductEntity = new ProductsEntity();
		newProductEntity.setId(20);
		newProductEntity.setName("New Product");
		newProductEntity.setPrice(BigDecimal.valueOf(20.00));

		CartEntity updatedEntity = new CartEntity();
		updatedEntity.setId(cartId);
		updatedEntity.setQuantity(updateDTO.getQuantity());
		updatedEntity.setProductsByProductId(newProductEntity);
		updatedEntity.setUsersByUserId(userEntity);
		updatedEntity.setCreatedAt(cartEntity.getCreatedAt());

		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
		when(productRepository.findById(20)).thenReturn(Optional.of(newProductEntity));
		when(userRepository.findById(cartDTO.getId())).thenReturn(Optional.of(userEntity));
		when(cartRepository.save(any(CartEntity.class))).thenReturn(updatedEntity);

		CartDTO updatedDTO = cartService.updateItemInCart(cartId, updateDTO);

		assertEquals(updateDTO.getQuantity(), updatedDTO.getQuantity());
		assertEquals(newProductDTO.getId(), updatedDTO.getProduct().getId());
		verify(cartRepository, times(1)).save(any(CartEntity.class));
	}

	@Test
	void updateItemInCart_NonExistingCartId_ThrowsNotFound() {
		int cartId = 99;
		when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

		assertThrows(NotFound.class, () -> cartService.updateItemInCart(cartId, new CartDTO()));
		verify(cartRepository, never()).save(any());
	}

	@Test
	void updateItemInCart_NewProductNotFound_ThrowsRuntimeException() {
		int cartId = 1;
		CartDTO updateDTO = new CartDTO();
		ProductDTO newProductDTO = new ProductDTO();
		newProductDTO.setId(99);
		updateDTO.setProduct(newProductDTO);

		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
		when(productRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> cartService.updateItemInCart(cartId, updateDTO));
		verify(cartRepository, never()).save(any());
	}

	@Test
	void getAllCarts_NoFilter_ReturnsPageOfCartDTOs() {
		List<CartEntity> cartEntities = Collections.singletonList(cartEntity);
		Page<CartEntity> cartPage = new PageImpl<>(cartEntities, PageRequest.of(0, 10), cartEntities.size());
		when(cartRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(cartPage);

		Page<CartDTO> cartDTOPage = cartService.getAllCarts(new CartFilterDTO(), PageRequest.of(0, 10));

		assertFalse(cartDTOPage.isEmpty());
		assertEquals(1, cartDTOPage.getContent().size());
		assertEquals(cartEntity.getId(), cartDTOPage.getContent().get(0).getId());
		verify(cartRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
	}
}