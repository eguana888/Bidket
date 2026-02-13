package com.example.auction.domain.bid.repository;

import com.example.auction.domain.bid.entity.Bid;
import com.example.auction.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid,Long> {

    Optional<Bid> findTopByProductOrderByBidPriceDesc(Product product);
}
