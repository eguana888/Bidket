package com.example.auction.domain.AuctionService;

import com.example.auction.domain.bid.entity.Bid;
import com.example.auction.domain.bid.entity.Bidstatus;
import com.example.auction.domain.bid.repository.BidRepository;
import com.example.auction.domain.order.entity.Order;
import com.example.auction.domain.order.entity.OrderStatus;
import com.example.auction.domain.order.repository.OrderRepository;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.ProductRepository;
import com.example.auction.domain.user.entity.User;
import com.example.auction.global.exception.CustomException;
import com.example.auction.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public void closeAuction(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));


        if (product.getStatus() != Bidstatus.ING) {
            return;
        }


        Optional<Bid> winningBidOp = bidRepository.findTopByProductOrderByBidPriceDesc(product);

        if (winningBidOp.isPresent()) {

            Bid ContractorBid = winningBidOp.get();
            User Contractor  = ContractorBid.getBidder();


            Order order = Order.builder()
                    .user(Contractor)
                    .product(product)
                    .finalPrice(ContractorBid.getBidPrice())
                    .status(OrderStatus.PENDING)
                    .build();

            orderRepository.save(order);


            product.changeStatus(Bidstatus.ENDED);

            log.info("낙찰. 상품: {}, 낙찰자: {}, 금액: {}",
                    product.getId(), Contractor.getName(), ContractorBid.getBidPrice());

        } else {
            product.changeStatus(Bidstatus.FAIL);
            log.info("유찰됨. 상품: {}", product.getId());
        }
    }
}
