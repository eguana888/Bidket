package com.example.auction.domain.product.controller;


import com.example.auction.domain.product.dto.ProductCreateRequest;
import com.example.auction.domain.product.dto.ProductDetailResponse;
import com.example.auction.domain.product.dto.ProductResponse;
import com.example.auction.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public ResponseEntity<Long> registerProduct(@RequestBody ProductCreateRequest request,
                                                @RequestParam Long sellerId) { // 테스트 편의상 Param으로 받음
        Long productId = productService.register(request, sellerId);
        return ResponseEntity.ok(productId);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse response = productService.getProduct(productId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        List<ProductResponse> responses = productService.searchProducts(keyword);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/getProducts")
    public ResponseEntity<Slice<ProductDetailResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        return ResponseEntity.ok(productService.getProductList(page, size, sort));
    }


}
