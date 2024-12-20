package com.example.shop.clothesshop.repository;

import com.example.shop.clothesshop.entities.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<Brand, String> {
}
