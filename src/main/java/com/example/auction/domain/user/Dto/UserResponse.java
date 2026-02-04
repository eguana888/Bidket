package com.example.auction.domain.user.Dto;

import com.example.auction.domain.user.entity.User;

public record UserResponse(
        Long id,
        String name

) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName()
        );
    }
}
