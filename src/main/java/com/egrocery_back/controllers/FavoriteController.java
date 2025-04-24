package com.egrocery_back.controllers;

import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.services.FavoriteService;
import com.egrocery_back.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UsersRepository userRepository;

    @PostMapping("/{productId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long productId, @AuthenticationPrincipal UsersEntity user) {
        favoriteService.addFavorite(user.getId().longValue(), productId); 
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long productId, @AuthenticationPrincipal UsersEntity user) {
        favoriteService.removeFavorite(user.getId().longValue(), productId); 
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductsEntity>> getFavorites(@AuthenticationPrincipal UsersEntity user) {
        List<ProductsEntity> favorites = favoriteService.getFavorites(user.getId().longValue()); // conversão aqui
        return ResponseEntity.ok(favorites);
    }
}
