package com.example.api.composite.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductCompositeService {

    @GetMapping(value = "/products-composite/{productId}", produces = "application/json")
    ProductAggregate getProduct(@PathVariable int productId);
}
