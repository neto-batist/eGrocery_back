package com.egrocery_back.controllers;

import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.dto.ProductFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductService productService;

	private ProductDTO productDTO;
	private ProductFilterDTO productFilterDTO;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		productDTO = new ProductDTO();
		productDTO.setId(1);
		productDTO.setName("Maçã");
		productDTO.setDescription("Maçã Gala");
		productDTO.setPrice(BigDecimal.valueOf(2.50));
		productDTO.setCategoryId(1);
		productFilterDTO = new ProductFilterDTO();
		pageable = PageRequest.of(0, 10);
	}

	@Test
	void getAllProducts_NoFilter_ReturnsOkWithProductPage() {
		List<ProductDTO> productsList = Collections.singletonList(productDTO);
		Page<ProductDTO> productPage = new PageImpl<>(productsList, pageable, productsList.size());
		when(productService.getAllProducts(any(ProductFilterDTO.class), any(Pageable.class))).thenReturn(productPage);

		ResponseEntity<Page<ProductDTO>> response = productController.getAllProducts(productFilterDTO, pageable);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(productPage, response.getBody());
		verify(productService, times(1)).getAllProducts(productFilterDTO, pageable);
	}

	@Test
	void getProductById_ValidId_ReturnsOkWithProductDTO() throws NotFound {
		int productId = 1;
		when(productService.getProductById(productId)).thenReturn(productDTO);

		ResponseEntity<ProductDTO> response = productController.getProductById(productId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(productDTO, response.getBody());
		verify(productService, times(1)).getProductById(productId);
	}

	@Test
	void getProductById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int productId = 1;
		when(productService.getProductById(productId)).thenThrow(new NotFound("Produto não encontrado", 1));

		assertThrows(NotFound.class, () -> productController.getProductById(productId));
		verify(productService, times(1)).getProductById(productId);
	}

	@Test
    void createProduct_ValidInput_ReturnsOkWithCreatedProductDTO() {
        when(productService.saveProduct(productDTO)).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(productService, times(1)).saveProduct(productDTO);
    }

	@Test
	void updateProduct_ValidInput_ReturnsOkWithUpdatedProductDTO() throws NotFound {
		int productId = 1;
		ProductDTO updatedProductDTO = new ProductDTO();
		updatedProductDTO.setId(productId);
		updatedProductDTO.setName("Banana");
		updatedProductDTO.setPrice(BigDecimal.valueOf(1.99));
		when(productService.updateProduct(productId, productDTO)).thenReturn(updatedProductDTO);

		ResponseEntity<ProductDTO> response = productController.updateProduct(productId, productDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedProductDTO, response.getBody());
		verify(productService, times(1)).updateProduct(productId, productDTO);
	}

	@Test
	void updateProduct_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int productId = 1;
		when(productService.updateProduct(productId, productDTO)).thenThrow(new NotFound("Produto não encontrado", 1));

		assertThrows(NotFound.class, () -> productController.updateProduct(productId, productDTO));
		verify(productService, times(1)).updateProduct(productId, productDTO);
	}

	@Test
	void deleteProduct_ValidId_ReturnsNoContent() throws NotFound {
		int productId = 1;
		doNothing().when(productService).deleteProduct(productId);

		ResponseEntity<Void> response = productController.deleteProduct(productId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(productService, times(1)).deleteProduct(productId);
	}

	@Test
	void deleteProduct_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int productId = 1;
		doThrow(new NotFound("Produto não encontrado", 1)).when(productService).deleteProduct(productId);

		assertThrows(NotFound.class, () -> productController.deleteProduct(productId));
		verify(productService, times(1)).deleteProduct(productId);
	}
}