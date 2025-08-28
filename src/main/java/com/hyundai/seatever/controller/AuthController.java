package com.hyundai.seatever.controller;

import com.hyundai.seatever.dto.request.LoginRequestDto;
import com.hyundai.seatever.dto.request.SignUpRequestDto;
import com.hyundai.seatever.dto.response.TokenDto;
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
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto requestDto) {
        // 서비스 레이어를 호출하여 회원가입 처리
        authService.signup(requestDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto requestDto) {
        // 서비스 레이어를 호출하여 로그인 처리 후 토큰을 응답
        return ResponseEntity.ok(authService.login(requestDto));
    }
}