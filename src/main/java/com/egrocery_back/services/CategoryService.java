package com.egrocery_back.services;

import com.egrocery_back.dto.CategoryDTO;
import com.egrocery_back.dto.CategoryFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO saveCategory(CategoryDTO dto);
    Page<CategoryDTO> getAllCategories(CategoryFilterDTO filter, Pageable pageable);
    CategoryDTO getCategoryById(Integer id) throws NotFound;
    CategoryDTO updateCategory(Integer id, CategoryDTO dto) throws NotFound;
    void deleteCategory(Integer id) throws NotFound;
}

