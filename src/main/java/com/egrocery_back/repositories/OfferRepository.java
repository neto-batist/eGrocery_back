package com.egrocery_back.repositories;

import com.egrocery_back.models.OffersEntity;
import com.egrocery_back.models.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferRepository extends JpaRepository<OffersEntity, Integer>
        , JpaSpecificationExecutor<OffersEntity> {
}
