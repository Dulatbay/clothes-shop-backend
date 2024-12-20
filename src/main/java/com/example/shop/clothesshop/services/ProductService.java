package com.example.shop.clothesshop.services;

import com.example.shop.clothesshop.dtos.params.ProductSearchParams;
import com.example.shop.clothesshop.dtos.request.ProductCreateRequest;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    void create(@Valid ProductCreateRequest productCreateRequest);

    void delete(String id);

    void update(String productId, @Valid ProductCreateRequest productUpdateRequest);

    Page<ProductResponse> getAll(@Valid ProductSearchParams productSearchParams);

    ProductResponse getById(String id);
}
