package com.example.auction.domain.product.dto;

import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.user.entity.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Date;

public record ProductCreateRequest(
        @NotNull
        String title,

        @NotNull
        Long startPrice,

        @NotNull
        Long minBidIncrement,

        @NotNull
        LocalDateTime endAt
) {
    public Product toEntity(User seller) {
        return Product.builder()
                .user(seller)
                .title(this.title)
                .startPrice(this.startPrice)
                .minBidIncrement(this.minBidIncrement)
                .endAt(java.sql.Timestamp.valueOf(this.endAt))
                .createdAt(new Date())
                .build();
    }
}