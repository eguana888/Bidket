package com.example.auction.domain.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequest (
    @NotNull Long userId,
    @NotNull Long productId,
    @NotNull Long finalPrice

){}
