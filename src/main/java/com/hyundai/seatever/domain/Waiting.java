package com.hyundai.seatever.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "waitings")
public class Waiting {

    @Id
    private String id; // MongoDB가 자동으로 생성하는 고유 ID

    private Integer seatNumber;

    private String userEmail; // 웨이팅을 건 사용자의 이메일

    private LocalDateTime timestamp; // 웨이팅 등록 시간

    private Integer waitingCount; // 대기 인원 수
}