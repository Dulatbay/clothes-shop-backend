package com.example.shop.clothesshop.services;

import com.example.shop.clothesshop.dtos.request.BrandCreateRequest;
import com.example.shop.clothesshop.dtos.request.CategoryCreateRequest;
import com.example.shop.clothesshop.dtos.response.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    void create(@Valid CategoryCreateRequest categoryCreateRequest);

    void delete(String id);

    void update(String id, @Valid CategoryCreateRequest categoryCreateRequest);

    List<CategoryResponse> getAll();
}
