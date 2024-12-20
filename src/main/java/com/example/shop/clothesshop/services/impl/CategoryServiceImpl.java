package com.example.shop.clothesshop.services.impl;

import com.example.shop.clothesshop.dtos.request.CategoryCreateRequest;
import com.example.shop.clothesshop.dtos.response.CategoryResponse;
import com.example.shop.clothesshop.entities.Category;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.repository.CategoryRepository;
import com.example.shop.clothesshop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public void create(CategoryCreateRequest categoryCreateRequest) {
        Category category = new Category()
                .setName(categoryCreateRequest.getName())
                .setDescription(categoryCreateRequest.getDescription());

        categoryRepository.save(category);
    }

    @Override
    public void delete(String id) {
        var categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Category not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (categoryToDelete.isDeleted())
            throw new DbNotFoundException("Category deleted", HttpStatus.NOT_FOUND.getReasonPhrase());

        categoryToDelete.setDeleted(true);
        categoryRepository.save(categoryToDelete);
    }

    @Override
    public void update(String id, CategoryCreateRequest categoryCreateRequest) {
        var categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Category not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (categoryToDelete.isDeleted())
            throw new DbNotFoundException("Category not found", HttpStatus.NOT_FOUND.getReasonPhrase());


        categoryToDelete.setName(categoryCreateRequest.getName());
        categoryToDelete.setDescription(categoryCreateRequest.getDescription());

        categoryRepository.save(categoryToDelete);
    }

    @Override
    public List<CategoryResponse> getAll() {
        var categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    CategoryResponse categoryResponse = new CategoryResponse();
                    categoryResponse.setId(category.getId());
                    categoryResponse.setName(category.getName());
                    categoryResponse.setDescription(category.getDescription());
                    return categoryResponse;
                })
                .toList();
    }
}
