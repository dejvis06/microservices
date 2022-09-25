package com.example.api.core.review;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ReviewService {

    @GetMapping
    List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

    @PostMapping
    Review createReview(@RequestBody Review review);

    @DeleteMapping
    void deleteReviews(@RequestParam(value = "productId", required = true) int productId);
}
