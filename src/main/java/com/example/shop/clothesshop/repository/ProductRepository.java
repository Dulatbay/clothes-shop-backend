package com.example.shop.clothesshop.repository;

import com.example.shop.clothesshop.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByDeletedIsFalse();
    Optional<Product> findByIdAndDeletedIsFalse(String id);
}
