package com.example.auction.domain.product.repository;

import com.example.auction.domain.product.entity.AuctionBan;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionBanRepository extends JpaRepository<AuctionBan,Long> {
    boolean existsByProductAndUser(Product product, User user);
}
