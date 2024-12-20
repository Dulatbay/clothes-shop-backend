package com.example.shop.clothesshop.repository;

import com.example.shop.clothesshop.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findByProduct_IdAndDeletedIsFalse(String carId, Pageable pageable);
    Optional<Review> findByIdAndDeletedIsFalse(String id);
    @Aggregation(pipeline = {
            "{ $match: { 'product.$id': ObjectId(?0) } }",
            "{ $group: { _id: null, averageRating: { $avg: '$rating' } } }"
    })
    Double findAverageRatingByProductId(String productId);

}
