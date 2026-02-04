package com.example.auction.domain.order.service;

import com.example.auction.domain.order.dto.OrderRequest;
import com.example.auction.domain.order.entity.Order;
import com.example.auction.domain.order.repository.OrderRepository;
import com.example.auction.domain.product.entity.Product;
import com.example.auction.domain.product.repository.ProductRepository;
import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.user.repository.UserRepository;
import com.example.auction.global.exception.CustomException;
import com.example.auction.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createOrder(OrderRequest orderRequest) {

        User user = userRepository.findById(orderRequest.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(orderRequest.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Order order = Order.builder()
                .user(user)
                .product(product)
                .finalPrice(orderRequest.finalPrice())
                .build();

        Order savedOrder = orderRepository.save(order);

        log.info("주문 생성 완료 - OrderId: {}, User: {}, Product: {}",
                savedOrder.getId(), user.getName(), product.getTitle());

        return savedOrder.getId();
    }
}
