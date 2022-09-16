package com.example.api.core.recommendation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecommendationService {

    @GetMapping
    List<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping
    Recommendation createRecommendation(@RequestBody Recommendation recommendation);

    @DeleteMapping
    void deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
