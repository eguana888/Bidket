package com.example.auction.domain.bid.dto;

import com.example.auction.domain.bid.entity.Bid;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record BidRequest(
        @NotNull Long productId,
        @NotNull Long bidderId,
        @NotNull @Positive Long bidPrice
) {
    public Bid toEntity(Product product, User bidder) {
        return Bid.builder()
                .product(product)
                .bidder(bidder)
                .bidPrice(this.bidPrice)
                .bidTime(LocalDateTime.now())
                .build();
    }


}