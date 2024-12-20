package com.example.shop.clothesshop.mappers;


import com.example.shop.clothesshop.dtos.request.ReviewCreateRequest;
import com.example.shop.clothesshop.dtos.response.ReviewResponse;
import com.example.shop.clothesshop.entities.Review;
import com.example.shop.clothesshop.repository.ProductRepository;
import com.example.shop.clothesshop.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class ReviewMapper {
    @Autowired
    private FileService fileService;
    @Autowired
    private ProductRepository productRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrls", qualifiedByName = "saveAndReturnUrl", source = "images")
    public abstract Review toEntity(ReviewCreateRequest reviewCreateRequest);

    @Mapping(target = "author", expression = "java(review.getAuthor().getUsername())")
    @Mapping(target = "authorId", expression = "java(review.getAuthor().getId())")
    @Mapping(target = "product", expression = "java(review.getProduct().getName())")
    @Mapping(target = "productId", expression = "java(review.getProduct().getId())")
    public abstract ReviewResponse toResponse(Review review);

    @Named("saveAndReturnUrl")
    protected List<String> saveAndReturnUrl(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        files.forEach(file -> {
            urls.add(fileService.save(file));
        });

        return urls;
    }
}
