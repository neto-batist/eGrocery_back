package com.egrocery_back.controllers;

import com.egrocery_back.dto.CategoryDTO;
import com.egrocery_back.dto.CategoryFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.CategoryService;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

	@InjectMocks
	private CategoryController categoryController;

	@Mock
	private CategoryService categoryService;

	private CategoryDTO categoryDTO;
	private CategoryFilterDTO categoryFilterDTO;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		categoryDTO = new CategoryDTO();
		categoryDTO.setId(1);
		categoryDTO.setName("Eletrônicos");
		categoryFilterDTO = new CategoryFilterDTO();
		pageable = PageRequest.of(0, 10);
	}

	@Test
	void getAllCategories_NoFilter_ReturnsOkWithCategoryPage() {
		List<CategoryDTO> categorias = Collections.singletonList(categoryDTO);
		Page<CategoryDTO> pagina = new PageImpl<>(categorias, pageable, categorias.size());
		when(categoryService.getAllCategories(any(CategoryFilterDTO.class), any(Pageable.class))).thenReturn(pagina);

		ResponseEntity<Page<CategoryDTO>> retorno = categoryController.getAllCategories(categoryFilterDTO, pageable);

		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals(pagina, retorno.getBody());
		verify(categoryService, times(1)).getAllCategories(categoryFilterDTO, pageable);
	}

	@Test
	void getCategoryById_ValidId_ReturnsOkWithCategoryDTO() throws NotFound {
		int caategoriaId = 1;
		when(categoryService.getCategoryById(caategoriaId)).thenReturn(categoryDTO);

		ResponseEntity<CategoryDTO> retorno = categoryController.getCategoryById(caategoriaId);

		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals(categoryDTO, retorno.getBody());
		verify(categoryService, times(1)).getCategoryById(caategoriaId);
	}

	@Test
	void getCategoryById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int caategoriaId = 1;
		when(categoryService.getCategoryById(caategoriaId)).thenThrow(new NotFound("Categoria não encontrada", 1));

		assertThrows(NotFound.class, () -> categoryController.getCategoryById(caategoriaId));
		verify(categoryService, times(1)).getCategoryById(caategoriaId);
	}

	@Test
    void createCategory_ValidInput_ReturnsOkWithCreatedCategoryDTO() {
        when(categoryService.saveCategory(categoryDTO)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> retorno = categoryController.createCategory(categoryDTO);

        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertEquals(categoryDTO, retorno.getBody());
        verify(categoryService, times(1)).saveCategory(categoryDTO);
    }

	@Test
	void updateCategory_ValidInput_ReturnsOkWithUpdatedCategoryDTO() throws NotFound {
		int caategoriaId = 1;
		CategoryDTO updtCategoryDTO = new CategoryDTO();
		updtCategoryDTO.setId(caategoriaId);
		updtCategoryDTO.setName("Novos");
		when(categoryService.updateCategory(caategoriaId, categoryDTO)).thenReturn(updtCategoryDTO);

		ResponseEntity<CategoryDTO> retorno = categoryController.updateCategory(caategoriaId, categoryDTO);

		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals(updtCategoryDTO, retorno.getBody());
		verify(categoryService, times(1)).updateCategory(caategoriaId, categoryDTO);
	}

	@Test
	void updateCategory_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int caategoriaId = 1;
		when(categoryService.updateCategory(caategoriaId, categoryDTO))
				.thenThrow(new NotFound("Categoria não encontrada", 1));

		assertThrows(NotFound.class, () -> categoryController.updateCategory(caategoriaId, categoryDTO));
		verify(categoryService, times(1)).updateCategory(caategoriaId, categoryDTO);
	}

	@Test
	void deleteCategory_ValidId_ReturnsNoContent() throws NotFound {
		int caategoriaId = 1;
		doNothing().when(categoryService).deleteCategory(caategoriaId);

		ResponseEntity<Void> retorno = categoryController.deleteCategory(caategoriaId);

		assertEquals(HttpStatus.NO_CONTENT, retorno.getStatusCode());
		verify(categoryService, times(1)).deleteCategory(caategoriaId);
	}

	@Test
	void deleteCategory_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int caategoriaId = 1;
		doThrow(new NotFound("Categoria não encontrada", 1)).when(categoryService).deleteCategory(caategoriaId);

		assertThrows(NotFound.class, () -> categoryController.deleteCategory(caategoriaId));
		verify(categoryService, times(1)).deleteCategory(caategoriaId);
	}

}