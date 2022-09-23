package com.example.api.core.product;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface ProductService {

    @GetMapping(value = "/{productId}", produces = "application/json")
    Mono<Product> getProduct(@PathVariable int productId);

    @PostMapping
    Mono<Product> createProduct(@RequestBody Product product);

    @DeleteMapping(value = "/{productId}")
    Mono<Void> deleteProduct(@PathVariable int productId);
}