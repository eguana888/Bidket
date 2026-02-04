package com.example.auction.domain.bid.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Bidstatus {

    PREPARING("준비 중"),
    ING("경매 진행 중"),
    ENDED("낙찰 완료"),
    FAIL("유찰"),
    CANCELLED("경매 취소");

    private final String description;
}
