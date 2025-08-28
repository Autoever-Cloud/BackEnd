package com.hyundai.seatever.config;

import com.hyundai.seatever.jwt.JwtAccessDeniedHandler;
import com.hyundai.seatever.jwt.JwtAuthenticationEntryPoint;
import com.hyundai.seatever.jwt.JwtSecurityConfig;
import com.hyundai.seatever.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정 Disable
                .csrf(csrf -> csrf.disable())

                // 예외 처리 핸들러 등록
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // 세션을 사용하지 않으므로 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // API 경로별 접근 권한 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll() // '/auth/'로 시작하는 경로는 모두 허용
                                .anyRequest().authenticated() // 나머지는 모두 인증 필요
                )

                // JWT 필터를 Security 로직에 적용
                .with(new JwtSecurityConfig(jwtTokenProvider), customizer -> {});

        return http.build();
    }
}