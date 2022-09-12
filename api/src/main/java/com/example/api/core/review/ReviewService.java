package com.example.api.core.review;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ReviewService {

    @GetMapping
    List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);
}
