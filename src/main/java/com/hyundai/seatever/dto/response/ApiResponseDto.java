package com.hyundai.seatever.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 모든 API 응답을 이 형식으로 감싸서 반환
@Getter
@RequiredArgsConstructor
public class ApiResponseDto<T> {
    private final boolean success;
    private final T data;

    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(true, data);
    }

    public static <T> ApiResponseDto<T> fail(T data) {
        return new ApiResponseDto<>(false, data);
    }
}