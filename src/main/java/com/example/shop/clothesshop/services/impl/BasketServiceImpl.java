package com.example.shop.clothesshop.services.impl;


import com.example.shop.clothesshop.dtos.params.PaginationParams;
import com.example.shop.clothesshop.dtos.response.ProductResponse;
import com.example.shop.clothesshop.entities.BasketItem;
import com.example.shop.clothesshop.entities.User;
import com.example.shop.clothesshop.exceptions.DbNotFoundException;
import com.example.shop.clothesshop.mappers.ProductMapper;
import com.example.shop.clothesshop.repository.BasketItemRepository;
import com.example.shop.clothesshop.repository.ProductRepository;
import com.example.shop.clothesshop.services.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketItemRepository basketItemRepository;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;


    @Override
    public Page<ProductResponse> getBasketByUser(String userId, PaginationParams paginationParams) {
        var pageable = PageRequest.of(paginationParams.getPage(), paginationParams.getSize());

        return basketItemRepository.findByAuthor_Id(userId, pageable)
                .map(productMapper::toBasketResponse);
    }

    @Override
    @Transactional
    public void deleteFromBasket(User author, String productId, Integer quantity) {
        var product = productRepository.findByIdAndDeletedIsFalse(productId)
                .orElseThrow(() -> new DbNotFoundException("Product not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        var basketItem = basketItemRepository.findByAuthor_IdAndProduct_Id(author.getId(), productId)
                .orElseThrow(() -> new DbNotFoundException("Product not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        if (basketItem.getQuantity() < quantity)
            throw new IllegalArgumentException("Request to delete quantity bigger");

        basketItem.setQuantity(basketItem.getQuantity() - quantity);
        basketItemRepository.save(basketItem);

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void addToBasket(User author, String productId, Integer quantity) {
        var product = productRepository.findByIdAndDeletedIsFalse(productId)
                .orElseThrow(() -> new DbNotFoundException("Product not found", HttpStatus.BAD_REQUEST.getReasonPhrase()));

        var basketItemOptional = basketItemRepository.findByAuthor_IdAndProduct_Id(author.getId(), productId);

        if(product.getQuantity() < quantity)
            throw new IllegalArgumentException("Request to add quantity bigger");


        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        if(basketItemOptional.isEmpty()){
            var basketItem = new BasketItem()
                    .setAuthor(author)
                    .setProduct(product)
                    .setQuantity(quantity);

            basketItemRepository.save(basketItem);
            return;
        }


        var basketItem = basketItemOptional.get();
        basketItem.setQuantity(basketItem.getQuantity() + quantity);
        basketItemRepository.save(basketItem);
    }
}
