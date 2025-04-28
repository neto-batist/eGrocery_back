package com.egrocery_back.services;

import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.dto.ProductFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.CategoriesEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.repositories.CategoryRepository;
import com.egrocery_back.repositories.ProductRepository;
import com.egrocery_back.specifications.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductsEntity mapToEntity(ProductDTO dto) {
        ProductsEntity entity = new ProductsEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setImageUrl(dto.getImageUrl());
        entity.setCreatedAt(dto.getCreatedAt());

        if (dto.getCategoryId() != null) {
            CategoriesEntity category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + dto.getCategoryId()));
            entity.setCategoriesByCategoryId(category);
        }

        return entity;
    }

    public ProductDTO mapToDTO(ProductsEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getCategoriesByCategoryId() != null) {
            dto.setCategoryId(entity.getCategoriesByCategoryId().getId());
        }

        return dto;
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        ProductsEntity product = mapToEntity(productDTO);
        ProductsEntity savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    public Page<ProductDTO> getAllProducts(ProductFilterDTO filter, Pageable pageable) {
        Specification<ProductsEntity> spec = Specification
                .where(ProductSpecification.nameContains(filter.getName()))
                .and(ProductSpecification.categoryEquals(filter.getCategoryId()))
                .and(ProductSpecification.priceGreaterThanOrEqual(filter.getMinPrice()))
                .and(ProductSpecification.priceLessThanOrEqual(filter.getMaxPrice()));

        return productRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public ProductDTO getProductById(Integer id) throws NotFound {
        ProductsEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFound("Produto", id));
        return mapToDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws NotFound {
        ProductsEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFound("Produto", id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStockQuantity(productDTO.getStockQuantity());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setCreatedAt(productDTO.getCreatedAt());

        if (productDTO.getCategoryId() != null) {
            CategoriesEntity category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + productDTO.getCategoryId()));
            existingProduct.setCategoriesByCategoryId(category);
        }

        ProductsEntity updatedProduct = productRepository.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) throws NotFound {
        ProductsEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFound("Produto", id));
        productRepository.delete(existingProduct);
    }
}
