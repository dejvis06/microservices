package com.example.api.core.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;

}