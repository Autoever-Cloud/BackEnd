package com.hyundai.seatever.controller;

import com.hyundai.seatever.dto.request.LoginRequestDto;
import com.hyundai.seatever.dto.request.SignUpRequestDto;
import com.hyundai.seatever.dto.response.ApiResponseDto;
import com.hyundai.seatever.dto.response.LoginResponseDto;
import com.hyundai.seatever.dto.response.MessageResponseDto;
import com.hyundai.seatever.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<MessageResponseDto>> signup(@RequestBody SignUpRequestDto requestDto) {
        // 1. 서비스 호출
        authService.signup(requestDto);
        // 2. 명세서에 맞는 응답 데이터 생성
        MessageResponseDto responseData = new MessageResponseDto("회원가입이 성공적으로 완료되었습니다.");
        // 3. ApiResponseDto로 감싸서 최종 응답 반환
        return ResponseEntity.ok(ApiResponseDto.success(responseData));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto requestDto) {
        // 1. 서비스 호출
        LoginResponseDto responseData = authService.login(requestDto);
        // 2. ApiResponseDto로 감싸서 최종 응답 반환
        return ResponseEntity.ok(ApiResponseDto.success(responseData));
    }
}