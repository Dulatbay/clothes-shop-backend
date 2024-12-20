package com.example.shop.clothesshop.entities;

import com.example.shop.clothesshop.entities.base.BaseEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "reviews")
@Accessors(chain = true)
@Getter
@Setter
public class Review extends BaseEntity {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @NotNull
    private String content;

    @Min(0)
    @Max(5)
    private double rating;

    @DBRef
    private User author;

    @DBRef
    private Product product;

    private List<String> imageUrls;
}
