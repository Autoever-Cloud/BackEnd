package com.hyundai.seatever.dto.request;

import com.hyundai.seatever.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String contact; // 명세서에 맞게 phoneNumber -> contact로 변경

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password)) // 비밀번호 암호화
                .name(this.name)
                .contact(this.contact)
                .build();
    }
}