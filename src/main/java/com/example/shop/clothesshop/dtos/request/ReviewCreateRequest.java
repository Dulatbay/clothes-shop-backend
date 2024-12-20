package com.example.shop.clothesshop.dtos.request;

import com.example.shop.clothesshop.entities.Product;
import com.example.shop.clothesshop.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewCreateRequest {
    @NotBlank
    private String content;

    @Min(0)
    @Max(5)
    private double rating;

    @NotBlank
    private String productId;

    private List<MultipartFile> images = new ArrayList<>();

    @JsonIgnore
    private User author;

    @JsonIgnore
    private Product product;
}
