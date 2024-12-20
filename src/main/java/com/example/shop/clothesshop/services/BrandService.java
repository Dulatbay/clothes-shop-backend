package com.example.shop.clothesshop.services;

import com.example.shop.clothesshop.dtos.params.BrandSearchParams;
import com.example.shop.clothesshop.dtos.request.BrandCreateRequest;
import com.example.shop.clothesshop.dtos.response.BrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BrandService {
    void create(BrandCreateRequest brandCreateRequest);

    void delete(String id);

    void update(String id, BrandCreateRequest carBrandUpdateRequest);

    Page<BrandResponse> getAll(BrandSearchParams brandSearchParams);
}
