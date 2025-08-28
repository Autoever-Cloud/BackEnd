package com.hyundai.seatever.service;

import com.hyundai.seatever.domain.User;
import com.hyundai.seatever.dto.request.LoginRequestDto;
import com.hyundai.seatever.dto.request.SignUpRequestDto;
import com.hyundai.seatever.dto.response.TokenDto;
import com.hyundai.seatever.jwt.JwtTokenProvider;
import com.hyundai.seatever.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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
    public User signup(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        User user = signUpRequestDto.toEntity(passwordEncoder);
        return userRepository.save(user);
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장 (추후 구현)

        // 5. 토큰 발급
        return tokenDto;
    }
}