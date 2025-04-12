package com.egrocery_back.services;

import com.egrocery_back.dto.CategoryDTO;
import com.egrocery_back.dto.CategoryFilterDTO;
import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.CategoriesEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.repositories.CategoryRepository;
import com.egrocery_back.specifications.CategorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductServiceImpl productService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductServiceImpl productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    private CategoryDTO mapToDTO(CategoriesEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedAt(entity.getCreatedAt());
        if (entity.getProductsById() != null) {
            List<ProductDTO> productDTOs = entity.getProductsById()
                    .stream()
                    .map(productService::mapToDTO) // se mapToDTO for private, mova para um mapper ou torne público
                    .collect(Collectors.toList());

            dto.setProducts(productDTOs);
        }
        return dto;
    }

    private CategoriesEntity mapToEntity(CategoryDTO dto) {
        CategoriesEntity entity = new CategoriesEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCreatedAt(dto.getCreatedAt());

        if (dto.getProducts() != null) {
            List<ProductsEntity> products = dto.getProducts()
                    .stream()
                    .map(productService::mapToEntity) // idem, cuidado com visibilidade
                    .collect(Collectors.toList());
            entity.setProductsById(products);
        }
        return entity;
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO dto) {
        CategoriesEntity entity = mapToEntity(dto);
        entity.setCreatedAt(Timestamp.from(Instant.now()));
        CategoriesEntity saved = categoryRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public Page<CategoryDTO> getAllCategories(CategoryFilterDTO filter, Pageable pageable) {
        Specification<CategoriesEntity> spec = Specification
                .where(CategorySpecification.nameContains(filter.getName()));

        return categoryRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) throws NotFound {
        CategoriesEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFound("Categoria", id));
        return mapToDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Integer id, CategoryDTO dto) throws NotFound {
        CategoriesEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFound("Categoria", id));

        category.setName(dto.getName());

        CategoriesEntity updated = categoryRepository.save(category);
        return mapToDTO(updated);
    }

    @Override
    public void deleteCategory(Integer id) throws NotFound {
        CategoriesEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFound("Categoria", id));
        categoryRepository.delete(category);
    }
}
