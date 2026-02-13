package com.example.auction.domain.product.dto;

import com.example.auction.domain.product.entity.Product;

import java.time.LocalDateTime;

public record ProductDetailResponse(
        Long id,
        String title,
        Long startPrice,
        Long currentPrice, // 현재가
        LocalDateTime endAt,
        String status
) {
    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getTitle(),
                product.getStartPrice(),
                product.getCurrentPrice(),
                product.getEndAt(),
                product.getStatus().name()
        );
    }
}
