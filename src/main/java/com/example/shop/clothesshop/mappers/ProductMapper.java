package com.example.shop.clothesshop.mappers;



import com.example.shop.clothesshop.dtos.request.ProductCreateRequest;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import com.example.shop.clothesshop.entities.BasketItem;
import com.example.shop.clothesshop.entities.Brand;
import com.example.shop.clothesshop.entities.Category;
import com.example.shop.clothesshop.entities.Product;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.repository.BrandRepository;
import com.example.shop.clothesshop.repository.CategoryRepository;
import com.example.shop.clothesshop.services.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    protected FileService fileService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", source = "brandId", qualifiedByName = "findByBrandId")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "findByCategoryId")
    @Mapping(target = "imageUrls", qualifiedByName = "saveAndReturnUrl", source = "images")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "viewed", expression = "java(1)")
    public abstract Product toEntity(ProductCreateRequest productCreateRequest);

    @Mapping(target = "brandId", expression = "java(product.getBrand().getId())")
    @Mapping(target = "brandName", expression = "java(product.getBrand().getName())")
    @Mapping(target = "categoryId", expression = "java(product.getCategory().getId())")
    @Mapping(target = "categoryName", expression = "java(product.getCategory().getName())")
    public abstract ProductResponse toResponse(Product product);

    public ProductResponse toBasketResponse(BasketItem basketItem) {
        ProductResponse carResponse = toResponse(basketItem.getProduct());
        carResponse.setQuantity(basketItem.getQuantity());
        return carResponse;
    }

    @Named("saveAndReturnUrl")
    public List<String> saveAndReturnUrl(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        files.forEach(file -> {
            urls.add(fileService.save(file));
        });

        return urls;
    }

    @Named("findByBrandId")
    public Brand findByBrandId(String brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new DbNotFoundException("Incorrect brand", HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Named("findByCategoryId")
    public Category findByCategoryId(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DbNotFoundException("Incorrect category", HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    public void updateEntity(Product existingProduct,
                             ProductCreateRequest carUpdateRequest) {
        if (carUpdateRequest.getPrice() > 0) {
            existingProduct.setPrice(carUpdateRequest.getPrice());
        }
        if (carUpdateRequest.getImages() != null) {
            existingProduct.getImageUrls()
                    .forEach(img -> fileService.deleteByFileName(img));
            List<String> newUrls = new ArrayList<>();
            carUpdateRequest.getImages()
                    .forEach(img -> newUrls.add(fileService.save(img)));
            existingProduct.setImageUrls(newUrls);
        }
        if (carUpdateRequest.getDescription() != null) {
            existingProduct.setDescription(carUpdateRequest.getDescription());
        }
        if (carUpdateRequest.getBrandId() != null) {
            existingProduct.setBrand(findByBrandId(carUpdateRequest.getBrandId()));
        }
    }
}
