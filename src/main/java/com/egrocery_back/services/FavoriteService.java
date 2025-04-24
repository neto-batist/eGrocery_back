package com.egrocery_back.services;

import com.egrocery_back.models.ProductsEntity;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long userId, Long productId);
    void removeFavorite(Long userId, Long productId);
    List<ProductsEntity> getFavorites(Long userId);
}
