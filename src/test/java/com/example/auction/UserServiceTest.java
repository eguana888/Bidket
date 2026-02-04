package com.example.auction;

import com.example.auction.domain.user.Dto.UserJoinRequest;
import com.example.auction.domain.user.Dto.UserResponse;
import com.example.auction.domain.user.repository.UserRepository;
import com.example.auction.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception{


        UserJoinRequest request = new UserJoinRequest("l");

        // when
        UserResponse response = userService.join(request);

        // then
        assertThat(response.name()).isEqualTo("l");

    }
}
