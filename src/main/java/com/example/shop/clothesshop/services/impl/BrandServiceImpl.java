package com.example.shop.clothesshop.services.impl;


import com.example.shop.clothesshop.dtos.params.BrandSearchParams;
import com.example.shop.clothesshop.dtos.request.BrandCreateRequest;
import com.example.shop.clothesshop.dtos.response.BrandResponse;
import com.example.shop.clothesshop.entities.Brand;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.repository.BrandRepository;
import com.example.shop.clothesshop.services.BrandService;
import com.example.shop.clothesshop.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;

    @Override
    @Transactional
    public void create(BrandCreateRequest brandCreateRequest) {
        String url = fileService.save(brandCreateRequest.getImage());
        Brand brand = new Brand()
                .setName(brandCreateRequest.getName())
                .setDescription(brandCreateRequest.getDescription())
                .setImageUrl(url);

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void delete(String id) {
        var brandToDelete = brandRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Car brand not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (brandToDelete.isDeleted())
            throw new DbNotFoundException("Car brand deleted", HttpStatus.NOT_FOUND.getReasonPhrase());

        fileService.deleteByFileName(brandToDelete.getImageUrl());

        brandToDelete.setDeleted(true);
        brandRepository.save(brandToDelete);
    }

    @Override
    @Transactional
    public void update(String id, BrandCreateRequest brandUpdateRequest) {
        var brandToUpdate = brandRepository.findById(id)
                .orElseThrow(() -> new DbNotFoundException("Product brand not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

        if (brandToUpdate.isDeleted())
            throw new DbNotFoundException("Product brand not found", HttpStatus.NOT_FOUND.getReasonPhrase());


        fileService.deleteByFileName(brandToUpdate.getImageUrl());


        brandToUpdate.setImageUrl(fileService.save(brandUpdateRequest.getImage()));
        brandToUpdate.setName(brandUpdateRequest.getName());
        brandToUpdate.setDescription(brandUpdateRequest.getDescription());

        brandRepository.save(brandToUpdate);
    }

    @Override
    public List<BrandResponse> getAll(BrandSearchParams brandSearchParams) {

        var query = getCarBrandQueryByParams(brandSearchParams);

        var brands = mongoTemplate.find(query, Brand.class);

        return brands.stream()
                .map(brand -> new BrandResponse(brand.getId(), brand.getName(), brand.getDescription(), brand.getImageUrl()))
                .toList();
    }

    private static Query getCarBrandQueryByParams(BrandSearchParams brandSearchParams) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}
