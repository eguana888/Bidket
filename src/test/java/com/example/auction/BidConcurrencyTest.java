package com.example.auction;

import com.example.auction.domain.bid.dto.BidRequest;
import com.example.auction.domain.bid.service.BidService;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.ProductRepository;
import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.user.repository.UserRepository;
import com.example.auction.global.exception.CustomException;
import com.example.auction.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BidConcurrencyTest {

    @Autowired
    private BidService bidService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("동시에 100명이 입찰하면, 순서대로 처리되거나 실패해야 한다.")
    void raceConditionTest() throws InterruptedException {

        Long productId = 4L;
        Long userId = 4L;
        long fixedBidPrice = 110000L;

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);


        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger logicFailCount = new AtomicInteger();
        AtomicInteger deadlockCount = new AtomicInteger();
        AtomicInteger otherFailCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            int index = i + 1;
            executorService.submit(() -> {
                String threadName = "Thread-" + index;
                try {
                    BidRequest request = new BidRequest(productId, userId, fixedBidPrice);
                    bidService.bid(request);

                    System.out.println("[" + threadName + "] 입찰 성공!");
                    successCount.getAndIncrement();

                } catch (CustomException e) {

                    if (e.getErrorCode() == ErrorCode.INVALID_BID_PRICE) {
                        System.out.println("[" + threadName + "] 로직 방어 성공 (이미 가격이 높음)");
                        logicFailCount.getAndIncrement();
                    } else {
                        System.out.println("[" + threadName + "] 다른 CustomException: " + e.getErrorCode().getMessage());
                        otherFailCount.getAndIncrement();
                    }
                } catch (Exception e) {

                    if (e.getMessage() != null && (e.getMessage().contains("Deadlock") || e.getMessage().contains("deadlock"))) {
                        System.out.println("[" + threadName + "] 데드락 발생");
                        deadlockCount.getAndIncrement();
                    } else {
                        System.out.println("[" + threadName + "] 알 수 없는 에러: " + e.getMessage());
                        otherFailCount.getAndIncrement();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();


        assertThat(successCount.get()).isEqualTo(1);
        assertThat(logicFailCount.get()).isEqualTo(99);
        assertThat(deadlockCount.get()).isEqualTo(0);



    }
}