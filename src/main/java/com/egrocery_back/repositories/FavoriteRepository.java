package com.egrocery_back.repositories;

import com.egrocery_back.models.FavoriteEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.models.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    List<FavoriteEntity> findByUser(UsersEntity user);

    Optional<FavoriteEntity> findByUserAndProduct(UsersEntity user, ProductsEntity product);

    void deleteByUserAndProduct(UsersEntity user, ProductsEntity product);
}
