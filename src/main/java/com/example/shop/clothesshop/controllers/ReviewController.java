package com.example.shop.clothesshop.controllers;

import com.example.shop.clothesshop.constants.Utils;
import com.example.shop.clothesshop.dtos.request.ReviewCreateRequest;
import com.example.shop.clothesshop.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> createReview(@ModelAttribute
                                             @Valid
                                             ReviewCreateRequest reviewCreateRequest) {
        var author = Utils.getCurrentUser();

        reviewService.create(author, reviewCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        var author = Utils.getCurrentUser();

        reviewService.delete(author, reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
