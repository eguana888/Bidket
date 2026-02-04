package com.example.auction.domain.bid.entity;

import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "auto_bid",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_auto_bid_user_product", columnNames = {"user_id", "product_id"})
        }
)
public class AutoBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auto_bid_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, name = "limit_price")
    private Long limitPrice; // 나의 최대 한도액

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public AutoBid(User user, Product product, Long limitPrice) {
        this.user = user;
        this.product = product;
        this.limitPrice = limitPrice;
        this.createdAt = LocalDateTime.now();
    }
}