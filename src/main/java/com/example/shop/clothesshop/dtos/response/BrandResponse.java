package com.example.shop.clothesshop.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BrandResponse {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
}
