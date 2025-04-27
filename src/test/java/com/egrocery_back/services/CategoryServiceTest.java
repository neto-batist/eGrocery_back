package com.egrocery_back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.egrocery_back.dto.CategoryDTO;
import com.egrocery_back.dto.CategoryFilterDTO;
import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.CategoriesEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductServiceImpl productService;

    private CategoryDTO categoryDTO;
    private CategoriesEntity categoriesEntity;
    private ProductsEntity productsEntity;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productsEntity = new ProductsEntity();
        productsEntity.setId(1);
        productsEntity.setName("Product Test");

        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Product Test");

        categoriesEntity = new CategoriesEntity();
        categoriesEntity.setId(1);
        categoriesEntity.setName("Category Test");
        categoriesEntity.setCreatedAt(Timestamp.from(Instant.now()));
        categoriesEntity.setProductsById(Collections.singletonList(productsEntity));

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("Category Test");
        categoryDTO.setCreatedAt(Timestamp.from(Instant.now()));
        categoryDTO.setProducts(Collections.singletonList(productDTO));
    }

    @Test
    void saveCategory_ValidDTO_ReturnsSavedCategoryDTO() {
        when(categoryRepository.save(any(CategoriesEntity.class))).thenReturn(categoriesEntity);
        when(productService.mapToEntity(any(ProductDTO.class))).thenReturn(productsEntity);
        when(productService.mapToDTO(any(ProductsEntity.class))).thenReturn(productDTO);

        CategoryDTO savedDTO = categoryService.saveCategory(categoryDTO);

        assertNotNull(savedDTO);
        assertEquals(categoryDTO.getName(), savedDTO.getName());
        verify(categoryRepository, times(1)).save(any(CategoriesEntity.class));
    }

    @Test
    void getAllCategories_ReturnsPageOfCategoryDTOs() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoriesEntity> page = new PageImpl<>(Collections.singletonList(categoriesEntity));
        
        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(productService.mapToDTO(any(ProductsEntity.class))).thenReturn(productDTO);

        Page<CategoryDTO> result = categoryService.getAllCategories(new CategoryFilterDTO(), pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(categoryRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
    @Test
    void getCategoryById_ExistingId_ReturnsCategoryDTO() throws NotFound {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(categoriesEntity));
        when(productService.mapToDTO(any(ProductsEntity.class))).thenReturn(productDTO);

        CategoryDTO result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals(categoriesEntity.getName(), result.getName());
    }

    @Test
    void getCategoryById_NonExistingId_ThrowsNotFound() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> categoryService.getCategoryById(99));
    }

    @Test
    void updateCategory_ExistingId_ReturnsUpdatedCategoryDTO() throws NotFound {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(categoriesEntity));
        when(categoryRepository.save(any(CategoriesEntity.class))).thenReturn(categoriesEntity);
        when(productService.mapToDTO(any(ProductsEntity.class))).thenReturn(productDTO);

        CategoryDTO updatedDTO = new CategoryDTO();
        updatedDTO.setName("Updated Name");

        CategoryDTO result = categoryService.updateCategory(1, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
    }

    @Test
    void updateCategory_NonExistingId_ThrowsNotFound() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> categoryService.updateCategory(99, new CategoryDTO()));
    }

    @Test
    void deleteCategory_ExistingId_DeletesSuccessfully() throws NotFound {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(categoriesEntity));

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).delete(categoriesEntity);
    }

    @Test
    void deleteCategory_NonExistingId_ThrowsNotFound() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> categoryService.deleteCategory(99));
    }
}
