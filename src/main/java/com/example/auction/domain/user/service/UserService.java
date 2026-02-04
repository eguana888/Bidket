package com.example.auction.domain.user.service;

import com.example.auction.domain.user.Dto.UserJoinRequest;
import com.example.auction.domain.user.Dto.UserResponse;
import com.example.auction.domain.user.entity.User;
import com.example.auction.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse join(UserJoinRequest request) {
        validateDuplicateUser(request.name());

        User user = request.toEntity();

        userRepository.save(user);

        return UserResponse.from(user);
    }

    private void validateDuplicateUser(String name) {
        userRepository.findByName(name)
                .ifPresent(user -> {
                    throw new IllegalStateException("이미 존재하는 이름입니다: " + name);
                });
    }

}
