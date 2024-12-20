package com.example.shop.clothesshop.dtos.params;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandSearchParams extends PaginationParams {
    private String searchText;
}
