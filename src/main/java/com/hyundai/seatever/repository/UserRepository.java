package com.hyundai.seatever.repository;

import com.hyundai.seatever.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// 기존 MongoDB 인터페이스의 이름을 변경하고 패키지를 이동
public interface UserRepository extends MongoRepository<User, String> {
    // 이메일로 사용자를 찾는 메서드 (로그인, 중복 확인 시 사용)
    Optional<User> findByEmail(String email);
}