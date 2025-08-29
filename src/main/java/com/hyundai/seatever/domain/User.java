package com.hyundai.seatever.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder // 빌더 패턴을 사용하여 객체 생성을 더 명확하게 할 수 있다.
@NoArgsConstructor // JPA나 다른 프레임워크에서 객체를 생성할 때 필요하다.
@AllArgsConstructor // 빌더 패턴 사용 시 필요합니다.
@Document(collection = "users")
public class User {

	@Id // MongoDB의 자동 생성 _id와 매핑될 필드
	private String id;

	@Indexed(unique = true) // 이메일은 고유 값이어야 하므로 인덱스를 설정
	private String email;

	private String password;

	private String name;

	private String contact;

}