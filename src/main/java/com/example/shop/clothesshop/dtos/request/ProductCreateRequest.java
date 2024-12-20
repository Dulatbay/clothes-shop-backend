package com.example.shop.clothesshop.dtos.request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private Double price;
    private List<MultipartFile> images;
    private Integer quantity = 0;

    private String brandId;
    private String categoryId;
}
