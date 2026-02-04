package com.example.auction.domain.wishlist.service;

import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.ProductRepository;
import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.user.repository.UserRepository;
import com.example.auction.domain.wishlist.entity.Wishlist;
import com.example.auction.domain.wishlist.repository.WishlistRepository;
import com.example.auction.global.exception.CustomException;
import com.example.auction.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public String toggleWishlist(Long userId, Long productId) {

        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {

            wishlistRepository.deleteByUserIdAndProductId(userId, productId);
            return "찜 취소 완료";
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        wishlistRepository.save(wishlist);
        return "찜 등록 완료";
    }
}