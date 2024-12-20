package com.example.shop.clothesshop.repository;

import com.example.shop.clothesshop.entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
