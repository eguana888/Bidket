package com.example.auction.domain.product.service;

import com.example.auction.domain.product.dto.ProductCreateRequest;
import com.example.auction.domain.product.dto.ProductResponse;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.ProductRepository;
import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.user.repository.UserRepository;
import com.example.auction.global.exception.CustomException;
import com.example.auction.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(UserRepository userRepository,ProductRepository productRepository){
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long register(ProductCreateRequest request, Long sellerId) {

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product product = request.toEntity(seller);

        productRepository.save(product);

        return product.getId();
    }


    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);

    }

    //풀스캔 문제..
    public List<ProductResponse> searchProducts(String title) {
        List<Product> products = productRepository.findByTitleContaining(title);

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
