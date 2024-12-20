package com.example.shop.clothesshop.dtos.params;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchParams extends PaginationParams {
    private Double minPrice;
    private Double maxPrice;
    private String searchText;
    private String brandId;
    private String categoryId;
}
