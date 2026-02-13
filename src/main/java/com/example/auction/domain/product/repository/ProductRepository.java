package com.example.auction.domain.product.repository;

import com.example.auction.domain.bid.entity.Bidstatus;
import com.example.auction.domain.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByTitleContaining(String title);
    List<Product> findAllByStatusAndEndAtBefore(Bidstatus status, LocalDateTime date);

    Slice<Product> findAllByStatus(Bidstatus status, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN Bid b ON b.product = p " +
            "WHERE p.status = :status " +
            "GROUP BY p " +
            "ORDER BY COUNT(b) DESC")
    Slice<Product> findAllOrderByBidCount(@Param("status") Bidstatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
}
