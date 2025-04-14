package com.egrocery_back.repositories;

import com.egrocery_back.models.FaqsEntity;
import com.egrocery_back.specifications.FaqsSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqsRepository extends JpaRepository<FaqsEntity, Integer>
        , JpaSpecificationExecutor<FaqsEntity> {
}
