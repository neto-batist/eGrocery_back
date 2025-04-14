package com.egrocery_back.services;

import com.egrocery_back.dto.CartDTO;
import com.egrocery_back.dto.CartFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartService {
    
    // Adiciona um item ao carrinho
    CartDTO addItemToCart(CartDTO cartDTO);

    // Remove um item do carrinho
    void removeItemFromCart(Integer cartId) throws NotFound;

    //atualiza carrinho
    CartDTO updateItemInCart(Integer cartId, CartDTO cartDTO) throws NotFound;

    // Calcula o total do carrinho
    double calculateTotal(Integer cartId) throws NotFound;

    // Obtém todos os carrinhos com filtros aplicados
    Page<CartDTO> getAllCarts(CartFilterDTO filter, Pageable pageable);
}
