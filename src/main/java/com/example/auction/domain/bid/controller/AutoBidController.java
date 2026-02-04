package com.example.auction.domain.bid.controller;

import com.example.auction.domain.bid.dto.AutoBidRequest;
import com.example.auction.domain.bid.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autobid")
@RequiredArgsConstructor
public class AutoBidController {
    private final BidService bidService;

    @PostMapping
    public ResponseEntity<String> registerAutoBid(@RequestBody @Valid AutoBidRequest request) {
        bidService.registerAutoBid(request);
        return ResponseEntity.ok("자동 입찰 등록 완료");
    }
}
