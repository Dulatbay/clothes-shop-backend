package com.example.shop.clothesshop.services.impl;

import com.example.shop.clothesshop.dtos.params.ProductSearchParams;
import com.example.shop.clothesshop.dtos.request.ProductCreateRequest;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import com.example.shop.clothesshop.entities.Product;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.mappers.ProductMapper;
import com.example.shop.clothesshop.repository.ProductRepository;
import com.example.shop.clothesshop.services.FileService;
import com.example.shop.clothesshop.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;

    @Override
    public void create(ProductCreateRequest productCreateRequest) {
        Product toSave = productMapper.toEntity(productCreateRequest);
        productRepository.save(toSave);
    }

    @Override
    public void delete(String id) {
        var productToDelete = productRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Product not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (productToDelete.isDeleted())
            throw new DbNotFoundException("Product deleted", HttpStatus.NOT_FOUND.getReasonPhrase());

        productToDelete.getImageUrls()
                .forEach(fileService::deleteByFileName);
        productToDelete.setDeleted(true);
        productRepository.save(productToDelete);
    }

    @Override
    public void update(String productId, ProductCreateRequest productUpdateRequest) {
        var productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new DbNotFoundException("Product not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (productToUpdate.isDeleted())
            throw new DbNotFoundException("Product not found", HttpStatus.NOT_FOUND.getReasonPhrase());

        productMapper.updateEntity(productToUpdate, productUpdateRequest);

        productRepository.save(productToUpdate);
    }

    @Override
    public Page<ProductResponse> getAll(ProductSearchParams productSearchParams) {
        var pageable = PageRequest.of(productSearchParams.getPage(), productSearchParams.getSize());

        var query = getProductQueryByParams(productSearchParams)
                .with(pageable);

        var products = mongoTemplate.find(query, Product.class);
        long count = mongoTemplate.count(query.skip(0).limit(0), Product.class);

        List<String> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        if (!productIds.isEmpty()) {
            Query incrementQuery = new Query(Criteria.where("id").in(productIds));
            Update incrementUpdate = new Update().inc("viewed", 1);
            mongoTemplate.updateMulti(incrementQuery, incrementUpdate, Product.class);
        }

        List<ProductResponse> productResponses = products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(productResponses, pageable, count);
    }

    private static Query getProductQueryByParams(ProductSearchParams productSearchParams) {
        Query query = new Query();

        query.addCriteria(Criteria.where("deleted").is(false));

        if (productSearchParams.getMinPrice() != null || productSearchParams.getMaxPrice() != null) {
            Criteria priceCriteria = new Criteria("price");
            if (productSearchParams.getMinPrice() != null) {
                priceCriteria = priceCriteria.gte(productSearchParams.getMinPrice());
            }
            if (productSearchParams.getMaxPrice() != null) {
                priceCriteria = priceCriteria.lte(productSearchParams.getMaxPrice());
            }
            query.addCriteria(priceCriteria);
        }
        if (productSearchParams.getBrandId() != null) {
            query.addCriteria(Criteria.where("brand.id").is(productSearchParams.getBrandId()));
        }
        if (productSearchParams.getCategoryId() != null) {
            query.addCriteria(Criteria.where("category.id").is(productSearchParams.getCategoryId()));
        }
        if (productSearchParams.getSearchText() != null && !productSearchParams.getSearchText().isBlank()) {
            String searchText = productSearchParams.getSearchText();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("name").regex(searchText, "i"),
                    Criteria.where("brandName").regex(searchText, "i"),
                    Criteria.where("description").regex(searchText, "i")
            ));
        }

        return query;
    }
}
