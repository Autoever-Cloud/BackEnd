package com.hyundai.seatever.database;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hyundai.seatever.domain.User;

public interface MongoDB extends MongoRepository<User, String> {
}