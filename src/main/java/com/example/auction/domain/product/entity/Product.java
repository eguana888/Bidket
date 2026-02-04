package com.example.auction.domain.product.entity;

import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.bid.entity.Bidstatus;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private Bidstatus status;

    private Date endAt;

    private Date createdAt;

    private Date updatedAt;

    public void updateCurrentPrice(Long price) {
        this.currentPrice = price;
    }

}

