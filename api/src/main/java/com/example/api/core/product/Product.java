package com.example.api.core.product;

import lombok.Data;

@Data
public class Product {
    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;

}