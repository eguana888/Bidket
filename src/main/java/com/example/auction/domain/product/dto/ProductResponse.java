package com.example.auction.domain.product.dto;

import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.bid.entity.Bidstatus;

import java.time.LocalDateTime;
import java.util.Date;

public record ProductResponse(
        Long id,
        String sellerName,
        String title,
        Long startPrice,
        Long minBidIncrement,
        Bidstatus status,
        LocalDateTime endAt,
        Long currentPrice
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getUser().getName(),
                product.getTitle(),
                product.getStartPrice(),
                product.getMinBidIncrement(),
                product.getStatus(),
                product.getEndAt(),
                product.getCurrentPrice()
        );
    }
}