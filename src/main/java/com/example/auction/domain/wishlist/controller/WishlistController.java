package com.example.auction.domain.wishlist.controller;

import com.example.auction.domain.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;


    @PostMapping("/api/wishlist/{productId}")
    public ResponseEntity<String> toggleWishlist(
            @PathVariable Long productId,
            @RequestParam Long userId
    ) {
        String result = wishlistService.toggleWishlist(userId, productId);
        return ResponseEntity.ok(result);
    }
}