package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import org.springframework.stereotype.Service;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.User;
import com.side.domain.repository.UserRepositoryManager;

@Service
public class UserService {

	public User findByUserId(String userId) {
		return UserRepositoryManager.getUserRepository(JOOQ).findByUserId(userId);
	}

	public void create(User user) {
		UserRepositoryManager.getDefaultUserRepository().create(user);
	}

	public User loadUserByUserId(String userId) {
		return UserRepositoryManager.getDefaultUserRepository().findByUserIdAndStatus(userId, UserStatus.ACTIVE);
	}
}

