package com.example.shop.clothesshop.controllers;

import com.example.shop.clothesshop.dtos.request.CategoryCreateRequest;
import com.example.shop.clothesshop.dtos.response.CategoryResponse;
import com.example.shop.clothesshop.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> createBrand(@ModelAttribute
                                            @Valid
                                            CategoryCreateRequest categoryCreateRequest) {
        categoryService.create(categoryCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@PathVariable
                                       String id,
                                       @ModelAttribute
                                       @Valid
                                       CategoryCreateRequest categoryCreateRequest) {
        categoryService.update(id, categoryCreateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> categoryResponses = categoryService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponses);
    }


}
