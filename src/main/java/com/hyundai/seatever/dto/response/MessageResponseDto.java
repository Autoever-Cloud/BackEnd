package com.hyundai.seatever.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

// "message": "..." 형태의 간단한 메시지 응답에 사용
@Getter
@AllArgsConstructor
public class MessageResponseDto {
    private String message;
}