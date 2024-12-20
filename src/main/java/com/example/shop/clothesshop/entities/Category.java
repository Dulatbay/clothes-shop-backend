package com.example.shop.clothesshop.entities;

import com.example.shop.clothesshop.entities.base.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "categories")
@Accessors(chain = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String description;
}
