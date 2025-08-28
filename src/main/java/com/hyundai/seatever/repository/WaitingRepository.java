package com.hyundai.seatever.repository;

import com.hyundai.seatever.domain.Waiting;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WaitingRepository extends MongoRepository<Waiting, String> {
    // 특정 사용자의 모든 웨이팅 정보를 찾는 메서드
    List<Waiting> findByUserEmail(String userEmail);

    // 특정 좌석의 웨이팅 목록을 등록 시간 순으로 찾는 메서드
    List<Waiting> findBySeatNumberOrderByTimestampAsc(Integer seatNumber);
}