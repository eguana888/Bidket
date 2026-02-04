package com.example.auction.domain.bid.dto;
import jakarta.validation.constraints.NotNull;

public record AutoBidRequest(
        @NotNull Long userId,
        @NotNull Long productId,
        @NotNull Long limitPrice
) {}

