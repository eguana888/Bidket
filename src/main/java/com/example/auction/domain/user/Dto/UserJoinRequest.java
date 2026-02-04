package com.example.auction.domain.user.Dto;

import com.example.auction.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserJoinRequest(

        @NotNull
        String name

) {

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .build();
    }
}
