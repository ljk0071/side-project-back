package com.side.domain.repository;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.User;

public interface UserRepository {

	void create(User user);

	User findById(long uniqueId);

	User findByUserId(String userId);

	User findByUserIdAndStatus(String userId, UserStatus status);
}

