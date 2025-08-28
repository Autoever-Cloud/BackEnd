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
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id; // MongoDB가 자동으로 생성하는 고유 ID

    private Integer seatNumber;

    private String userEmail; // 예약을 한 사용자의 이메일

    private LocalDateTime bookingTime; // 예약 시간
}