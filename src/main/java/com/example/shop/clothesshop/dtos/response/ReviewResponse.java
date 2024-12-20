package com.example.shop.clothesshop.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewResponse {
    private String id;
    private String content;
    private double rating;
    private String author;
    private String authorId;
    private String product;
    private String productId;
    private List<String> imageUrls;
}
