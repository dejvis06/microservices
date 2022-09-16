package com.example.api.core.product;

import org.springframework.web.bind.annotation.*;

public interface ProductService {

    @GetMapping(value = "/{productId}", produces = "application/json")
    Product getProduct(@PathVariable int productId);

    @PostMapping
    Product createProduct(@RequestBody Product product);

    @DeleteMapping(value = "/{productId}")
    void deleteProduct(@PathVariable int productId);
}