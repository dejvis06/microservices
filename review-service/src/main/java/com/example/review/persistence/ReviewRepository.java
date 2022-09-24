package com.example.review.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

public interface ReviewRepository extends ReactiveCrudRepository<ReviewEntity, Integer> {

    @Transactional(readOnly = true)
    Flux<ReviewEntity> findByProductId(int productId);
}