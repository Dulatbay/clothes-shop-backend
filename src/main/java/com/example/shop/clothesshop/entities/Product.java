package com.example.shop.clothesshop.entities;

import com.example.shop.clothesshop.entities.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "products")
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String description;
    private Double price;
    private List<String> imageUrls;
    private int quantity = 0;
    private int viewed = 0;
    private double rating = 0.0;

    @DBRef
    private Brand brand;

    @DBRef
    private Category category;
}
