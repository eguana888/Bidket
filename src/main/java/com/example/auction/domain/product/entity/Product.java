package com.example.auction.domain.product.entity;

import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.bid.entity.Bidstatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String title;

    private Long startPrice;

    private Long currentPrice;

    private Long minBidIncrement;

    @Column(nullable = false)
    private int bidCount = 0;

    @Enumerated(EnumType.STRING)
    private Bidstatus status;

    private LocalDateTime endAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void updateCurrentPrice(Long price) {
        this.currentPrice = price;
    }
    public void changeStatus(Bidstatus bidstatus){
        status = bidstatus;
    }

    public void increaseBidCount() {
        this.bidCount++;
    }

    public int getBidCount() {
        return bidCount;
    }
}

