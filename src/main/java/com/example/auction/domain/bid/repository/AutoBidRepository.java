package com.example.auction.domain.bid.repository;

import com.example.auction.domain.bid.entity.AutoBid;
import com.example.auction.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoBidRepository extends JpaRepository<AutoBid,Long> {


    List<AutoBid> findTop2ByProductOrderByLimitPriceDesc(Product product);
}
