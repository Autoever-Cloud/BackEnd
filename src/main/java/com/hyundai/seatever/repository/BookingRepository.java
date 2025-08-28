package com.hyundai.seatever.repository;

import com.hyundai.seatever.domain.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    // 특정 사용자의 모든 예약을 찾는 메서드
    List<Booking> findByUserEmail(String userEmail);

    // 특정 시간대 사이의 모든 예약을 찾는 메서드
    List<Booking> findByBookingTimeBetween(LocalDateTime start, LocalDateTime end);
}