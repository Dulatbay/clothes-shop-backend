package com.example.shop.clothesshop.dtos.response;

import com.example.shop.clothesshop.entities.Brand;
import com.example.shop.clothesshop.entities.Category;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Double price;
    private List<String> imageUrls;
    private int quantity;
    private int viewed;
    private double rating;

    private String brandId;
    private String brandName;
    private String categoryId;
    private String categoryName;
}
