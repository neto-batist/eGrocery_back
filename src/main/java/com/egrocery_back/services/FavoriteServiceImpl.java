package com.egrocery_back.services;

import com.egrocery_back.models.FavoriteEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.repositories.FavoriteRepository;
import com.egrocery_back.repositories.ProductRepository;
import com.egrocery_back.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository userRepository;

    @Override
    public void addFavorite(Long userId, Long productId) {
        UsersEntity user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        ProductsEntity product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        boolean exists = favoriteRepository.findByUserAndProduct(user, product).isPresent();
        if (!exists) {
            FavoriteEntity favorite = new FavoriteEntity();
            favorite.setUser(user);
            favorite.setProduct(product);
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        UsersEntity user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        ProductsEntity product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        favoriteRepository.deleteByUserAndProduct(user, product);
    }

    @Override
    public List<ProductsEntity> getFavorites(Long userId) {
        UsersEntity user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return favoriteRepository.findByUser(user)
                .stream()
                .map(FavoriteEntity::getProduct)
                .collect(Collectors.toList());
    }
}
