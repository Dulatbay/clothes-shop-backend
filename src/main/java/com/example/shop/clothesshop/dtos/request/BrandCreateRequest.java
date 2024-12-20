package com.example.shop.clothesshop.dtos.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BrandCreateRequest {
    private String name;
    private String description;
    private MultipartFile image;
}
