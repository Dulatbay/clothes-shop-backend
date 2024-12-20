package com.example.shop.clothesshop.controllers;

import com.example.shop.clothesshop.dtos.base.PaginatedResponse;
import com.example.shop.clothesshop.dtos.params.BrandSearchParams;
import com.example.shop.clothesshop.dtos.request.BrandCreateRequest;
import com.example.shop.clothesshop.dtos.response.BrandResponse;
import com.example.shop.clothesshop.services.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> createBrand(@ModelAttribute
                                            @Valid
                                            BrandCreateRequest brandCreateRequest) {
        brandService.create(brandCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteCar(@PathVariable String productId) {
        brandService.delete(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "{product}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCar(@PathVariable
                                          String product,
                                          @ModelAttribute
                                          @Valid
                                          BrandCreateRequest carBrandUpdateRequest) {
        brandService.update(product, carBrandUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getCars(@ModelAttribute
                                                                        @Valid
                                                                    BrandSearchParams searchParams) {
        List<BrandResponse> products = brandService.getAll(searchParams);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


}
