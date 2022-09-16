package com.example.api.core.review;

import lombok.Data;

@Data
public class Review {

    private int productId;
    private int reviewId;
    private String author;
    private String subject;
    private String content;
    private String serviceAddress;
}
