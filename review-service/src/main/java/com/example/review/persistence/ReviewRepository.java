package com.example.review.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    @Transactional(readOnly = true)
    List<ReviewEntity> findByProductId(int productId);
}