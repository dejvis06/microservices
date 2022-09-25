package com.example.api.core.review;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

    @GetMapping
    Flux<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping
    Mono<Review> createReview(@RequestBody Review review);

    @DeleteMapping
    Mono<Void> deleteReviews(@RequestParam(value = "productId", required = true) int productId);
}
