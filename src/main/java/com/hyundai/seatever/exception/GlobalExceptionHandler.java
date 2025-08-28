package com.hyundai.seatever.exception;

import com.hyundai.seatever.dto.response.ApiResponseDto;
import com.hyundai.seatever.dto.response.MessageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice 어노테이션이 있어야 전역적으로 예외를 처리할 수 있다.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDto<MessageResponseDto>> handleCustomException(CustomException e) {
        // 1. 명세서에 맞는 실패 응답 데이터 생성
        MessageResponseDto responseData = new MessageResponseDto(e.getErrorCode().getMessage());
        // 2. ApiResponseDto로 감싸고, 에러 코드에 맞는 HTTP 상태와 함께 반환
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponseDto.fail(responseData));
    }
}
