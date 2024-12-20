package com.example.shop.clothesshop.controllers;

import com.example.shop.clothesshop.dtos.base.PaginatedResponse;
import com.example.shop.clothesshop.dtos.params.PaginationParams;
import com.example.shop.clothesshop.dtos.params.ProductSearchParams;
import com.example.shop.clothesshop.dtos.request.ProductCreateRequest;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import com.example.shop.clothesshop.dtos.response.ReviewResponse;
import com.example.shop.clothesshop.services.ProductService;

import com.example.shop.clothesshop.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> createProduct(@Valid
                                          @ModelAttribute
                                          ProductCreateRequest productCreateRequest) {
        productService.create(productCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.delete(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(@PathVariable
                                          String productId,
                                          @ModelAttribute
                                          @Valid
                                          ProductCreateRequest productUpdateRequest) {
        productService.update(productId, productUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ProductResponse>> getProducts(@ModelAttribute
                                                                      @Valid
                                                                      ProductSearchParams productSearchParams) {
        var products = productService.getAll(productSearchParams);

        return ResponseEntity.status(HttpStatus.OK).body(new PaginatedResponse<>(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        var product = productService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<PaginatedResponse<ReviewResponse>> getReviewsByProductId(@PathVariable
                                                                               String productId,
                                                                               @ModelAttribute
                                                                               @Valid
                                                                               PaginationParams paginationParams) {
        Page<ReviewResponse> reviews = reviewService.getByProduct(productId, paginationParams);

        return ResponseEntity.ok(new PaginatedResponse<>(reviews));
    }

}
