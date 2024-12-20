package com.example.shop.clothesshop.repository;

import com.example.shop.clothesshop.entities.BasketItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketItemRepository extends MongoRepository<BasketItem, String> {
    Page<BasketItem> findByAuthor_Id(String userId, Pageable pageable);
    Optional<BasketItem> findByAuthor_IdAndProduct_Id(String userId, String productId);
}
