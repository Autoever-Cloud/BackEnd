package com.hyundai.seatever.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hyundai.seatever.database.MongoDB;
import com.hyundai.seatever.info.User;

@RestController
public class UserController {

	@Autowired
	private MongoDB mongodb;
	
	// 사용자를 저장합니다.
	@PostMapping("/auth/signup")
	public User createUser(@RequestBody User user) {
		
		return mongodb.save(user);
	}
}