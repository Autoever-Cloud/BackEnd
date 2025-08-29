package com.hyundai.seatever.dto.response;

import com.hyundai.seatever.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private UserInfo user;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private String userId; // MongoDB의 ID
        private String email;
    }

    public static LoginResponseDto from(TokenDto tokenDto, User user) {
        UserInfo userInfo = new UserInfo(user.getId(), user.getEmail());
        return LoginResponseDto.builder()
                .token(tokenDto.getAccessToken())
                .user(userInfo)
                .build();
    }
}