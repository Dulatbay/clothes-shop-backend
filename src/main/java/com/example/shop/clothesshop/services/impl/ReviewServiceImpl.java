package com.example.shop.clothesshop.services.impl;

import com.example.shop.clothesshop.dtos.params.PaginationParams;
import com.example.shop.clothesshop.dtos.request.ReviewCreateRequest;
import com.example.shop.clothesshop.dtos.response.ReviewResponse;
import com.example.shop.clothesshop.entities.User;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.mappers.ReviewMapper;
import com.example.shop.clothesshop.repository.ProductRepository;
import com.example.shop.clothesshop.repository.ReviewRepository;
import com.example.shop.clothesshop.services.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void create(User author, ReviewCreateRequest reviewCreateRequest) {
        var product = productRepository.findByIdAndDeletedIsFalse(reviewCreateRequest.getProductId())
                .orElseThrow(() -> new DbNotFoundException("Product Not Found", HttpStatus.BAD_REQUEST.getReasonPhrase()));
        reviewCreateRequest.setAuthor(author);
        reviewCreateRequest.setProduct(product);

        var toSave = reviewMapper.toEntity(reviewCreateRequest);
        reviewRepository.save(toSave);

        var rating = reviewRepository.findAverageRatingByProductId(product.getId());
        log.info("Rating: {}", rating);

        product.setRating(rating);
        productRepository.save(product);
    }

    @Override
    public void delete(User author, String reviewId) {
        var reviewToDelete = reviewRepository.findByIdAndDeletedIsFalse(reviewId)
                .orElseThrow(() -> new DbNotFoundException("Review not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (!reviewToDelete.getAuthor().getId().equals(author.getId()) && !author.getRole().toString().equals("ADMIN"))
            throw new MethodNotAllowedException("You are not allowed to delete this review", Collections.singleton(HttpMethod.DELETE));

        reviewToDelete.setDeleted(true);
        reviewRepository.save(reviewToDelete);
    }

    public Page<ReviewResponse> getByProduct(String productId, PaginationParams paginationParams) {
        productRepository.findByIdAndDeletedIsFalse(productId)
                .orElseThrow(() -> new DbNotFoundException("Product not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        var reviews = reviewRepository.findByProduct_IdAndDeletedIsFalse(productId, PageRequest.of(paginationParams.getPage(), paginationParams.getSize()));

        return reviews
                .map(reviewMapper::toResponse);
    }

}
