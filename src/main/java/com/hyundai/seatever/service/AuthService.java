package com.hyundai.seatever.service;

import com.hyundai.seatever.domain.User;
import com.hyundai.seatever.dto.request.LoginRequestDto;
import com.hyundai.seatever.dto.request.SignUpRequestDto;
import com.hyundai.seatever.dto.response.LoginResponseDto;
import com.hyundai.seatever.dto.response.TokenDto;
import com.hyundai.seatever.exception.CustomException;
import com.hyundai.seatever.exception.ErrorCode;
import com.hyundai.seatever.jwt.JwtTokenProvider;
import com.hyundai.seatever.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = signUpRequestDto.toEntity(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            // 2. 비밀번호 체크
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

            // 4. 사용자 정보 조회
            User user = userRepository.findByEmail(loginRequestDto.getEmail())
                    .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

            // 5. 명세서에 맞는 응답 객체(LoginResponseDto)를 생성하여 반환
            return LoginResponseDto.from(tokenDto, user);

        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
    }
}