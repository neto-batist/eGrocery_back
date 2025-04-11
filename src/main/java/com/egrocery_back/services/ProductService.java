package com.egrocery_back.services;

import com.egrocery_back.dto.ProductDTO;
import com.egrocery_back.dto.ProductFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductDTO saveProduct(ProductDTO productDTO);
    Page<ProductDTO> getAllProducts(ProductFilterDTO filter, Pageable pageable);
    ProductDTO getProductById(Integer id) throws NotFound;
    ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws NotFound;
    void deleteProduct(Integer id) throws NotFound;
}
