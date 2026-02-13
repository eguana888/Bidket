package com.example.auction.domain.bid.service;

import com.example.auction.domain.bid.dto.AutoBidRequest;
import com.example.auction.domain.bid.dto.BidRequest;
import com.example.auction.domain.bid.entity.AutoBid;
import com.example.auction.domain.bid.entity.Bid;
import com.example.auction.domain.bid.repository.AutoBidRepository;
import com.example.auction.domain.bid.repository.BidRepository;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.AuctionBanRepository;
import com.example.auction.domain.product.repository.ProductRepository;
import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.user.repository.UserRepository;
import com.example.auction.global.exception.CustomException;
import com.example.auction.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class BidService {

    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AutoBidRepository autoBidRepository;
    private final AuctionBanRepository auctionBanRepository;

    @Transactional
    public void bid(BidRequest request) {
        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        validateAuctionTime(product);

        User bidder = userRepository.findById(request.bidderId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        if (auctionBanRepository.existsByProductAndUser(product, bidder)) {
            throw new CustomException(ErrorCode.BANNED_USER);
        }

        if (product.getCurrentPrice() >= request.bidPrice()) {
            throw new CustomException(ErrorCode.INVALID_BID_PRICE);
        }

        try { Thread.sleep(50); } catch (InterruptedException e) {}
        saveBid(product, bidder, request.bidPrice());

        processAutoBid(product);
    }

    @Transactional
    public void registerAutoBid(AutoBidRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        validateAuctionTime(product);

        autoBidRepository.save(AutoBid.builder()
                .user(user)
                .product(product)
                .limitPrice(request.limitPrice())
                .build());


        processAutoBid(product);
    }


    private void processAutoBid(Product product) {

        List<AutoBid> top2 = autoBidRepository.findTop2ByProductOrderByLimitPriceDesc(product);

        if (top2.isEmpty()) return;

        AutoBid king = top2.get(0);
        long currentPrice = product.getCurrentPrice();
        long increment = product.getMinBidIncrement();

        if (top2.size() == 1) {

            if (king.getLimitPrice() > currentPrice) {

                long nextPrice = currentPrice + increment;

                if (nextPrice <= king.getLimitPrice()) {
                    saveBid(product, king.getUser(), nextPrice);
                } else {

                    if (currentPrice < king.getLimitPrice()) {
                        saveBid(product, king.getUser(), king.getLimitPrice());
                    }
                }
            }
            return;
        }


        AutoBid viceKing = top2.get(1);


        long finalPrice = viceKing.getLimitPrice() + increment;


        if (finalPrice > king.getLimitPrice()) {
            finalPrice = king.getLimitPrice();
        }

        if (currentPrice < finalPrice) {
            saveBid(product, king.getUser(), finalPrice);
            System.out.println("입찰자: " + king.getUser().getName() + ", 가격: " + finalPrice);
        }
    }

    private void saveBid(Product product, User winner, long price) {

        product.updateCurrentPrice(price);
        product.increaseBidCount();

        bidRepository.save(Bid.builder()
                .product(product)
                .bidder(winner)
                .bidPrice(price)
                .build());
    }
    private void validateAuctionTime(Product product) {
        LocalDateTime now = LocalDateTime.now();


        if (product.getEndAt().isBefore(now)) {
            throw new CustomException(ErrorCode.AUCTION_ENDED);
        }
    }



}
