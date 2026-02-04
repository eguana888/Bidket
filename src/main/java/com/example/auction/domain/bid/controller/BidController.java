package com.example.auction.domain.bid.controller;

import com.example.auction.domain.bid.dto.BidRequest;
import com.example.auction.domain.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<String> bid(@RequestBody BidRequest request) {
        bidService.bid(request);
        return ResponseEntity.ok("입찰 성공");
    }
}