package com.example.auction.global.scheduler;

import com.example.auction.domain.AuctionService.AuctionService;
import com.example.auction.domain.bid.entity.Bidstatus;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final ProductRepository productRepository;
    private final AuctionService auctionService;

    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("스케줄러 실행: {}", now);


        List<Product> expiredProducts = productRepository.findAllByStatusAndEndAtBefore(Bidstatus.ING, now);

        if (expiredProducts.isEmpty()) {
            return;
        }


        for (Product product : expiredProducts) {
            try {
                auctionService.closeAuction(product.getId());
            } catch (Exception e) {
                log.error("상품 ID {} 마감 처리 중 오류 발생: {}", product.getId(), e.getMessage());
            }
        }
    }
}