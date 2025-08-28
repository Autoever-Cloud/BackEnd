package com.hyundai.seatever.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder // 빌더 패턴을 사용하여 객체 생성을 더 명확하게 할 수 있습니다.
@NoArgsConstructor // JPA나 다른 프레임워크에서 객체를 생성할 때 필요합니다.
@AllArgsConstructor // 빌더 패턴 사용 시 필요합니다.
@Document(collection = "users")
public class User {

	@Id // 기본 키를 email로 지정
	private String email;

	private String password;

	private String name;

	private String phoneNumber;

}

//package com.hyundai.seatever.domain;
//
//import java.io.Serializable;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//@Document(collection = "users")
//public class User implements Serializable {
//
//	@Id
//	private String name;
//	private String password;
//	private String phoneNumber;
//	private String email;
//
//	public User() {
//	}
//
//	public User(String name, String email, String password, String phoneNumber) {
//		this.name = name;
//		this.email = email;
//		this.password = password;
//		this.phoneNumber = phoneNumber;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getPhoneNumber() {
//		return phoneNumber;
//	}
//
//	public void setPhoneNumber(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//}