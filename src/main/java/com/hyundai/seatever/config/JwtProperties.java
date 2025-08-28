package com.hyundai.seatever.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt") // application.yaml의 'jwt' 접두사를 가진 설정을 매핑
@Getter
@Setter
public class JwtProperties {

    private String secret;
    private long expirationMs;

}
