package com.example.api.core.recommendation;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationService {

    @GetMapping
    Flux<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping
    Mono<Recommendation> createRecommendation(@RequestBody Recommendation recommendation);

    @DeleteMapping
    Mono<Void> deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
