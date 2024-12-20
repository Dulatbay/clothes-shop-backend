package com.example.shop.clothesshop.services;

import com.example.shop.clothesshop.dtos.params.PaginationParams;
import com.example.shop.clothesshop.dtos.request.ReviewCreateRequest;
import com.example.shop.clothesshop.dtos.response.ReviewResponse;
import com.example.shop.clothesshop.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {

    void create(User author, ReviewCreateRequest reviewCreateRequest);

    void delete(User author, String reviewId);

    Page<ReviewResponse> getByProduct(String productId, PaginationParams paginationParams);
}
