package com.example.shop.clothesshop.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {
    private String name;
    private String description;
}
