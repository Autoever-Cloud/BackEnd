package com.hyundai.seatever.exception;

import lombok.Getter;

// 비즈니스 로직에서 발생하는 예외를 처리하기 위한 커스텀 예외 클래스
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}