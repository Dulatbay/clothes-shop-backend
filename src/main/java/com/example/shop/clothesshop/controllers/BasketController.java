package com.example.shop.clothesshop.controllers;

import com.example.shop.clothesshop.constants.Utils;
import com.example.shop.clothesshop.dtos.base.PaginatedResponse;
import com.example.shop.clothesshop.dtos.params.PaginationParams;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import com.example.shop.clothesshop.services.BasketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/baskets")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/my")
    public ResponseEntity<PaginatedResponse<ProductResponse>> getCart(@ModelAttribute
                                                                  @Valid
                                                                      PaginationParams paginationParams) {
        var user = Utils.getCurrentUser();
        Page<ProductResponse> response = basketService.getBasketByUser(user.getId(), paginationParams);

        return ResponseEntity.ok(new PaginatedResponse<>(response));
    }

    @PostMapping("/my")
    public ResponseEntity<Void> createCart(@NotNull
                                           String productId,
                                           @NotNull
                                           @Min(1)
                                           Integer quantity) {
        var user = Utils.getCurrentUser();

        basketService.addToBasket(user, productId, quantity);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/my")
    public ResponseEntity<Void> deleteFromBasket(@NotNull
                                                 String productId,
                                                 @NotNull
                                                 @Min(1)
                                                 Integer quantity) {
        var user = Utils.getCurrentUser();

        basketService.deleteFromBasket(user, productId, quantity);

        return ResponseEntity.noContent().build();
    }
}
