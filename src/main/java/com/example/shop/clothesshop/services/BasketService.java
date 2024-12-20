package com.example.shop.clothesshop.services;


import com.example.shop.clothesshop.dtos.params.PaginationParams;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import com.example.shop.clothesshop.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BasketService {
    Page<ProductResponse> getBasketByUser(String userId, PaginationParams paginationParams);

    void deleteFromBasket(User author, String carId, Integer quantity);

    void addToBasket(User user,String carId, Integer quantity);
}
