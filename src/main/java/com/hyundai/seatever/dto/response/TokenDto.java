package com.hyundai.seatever.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType; // "Bearer"
    private String accessToken;
    private long accessTokenExpiresIn;
}