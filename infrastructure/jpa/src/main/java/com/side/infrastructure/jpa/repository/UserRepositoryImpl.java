package com.side.infrastructure.jpa.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jpa.mapper.UserMapper.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.User;
import com.side.domain.repository.UserRepository;
import com.side.domain.repository.UserRepositoryManager;
import com.side.infrastructure.jpa.entity.UserEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository repository;

	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		UserRepositoryManager.addUserRepository(JPA, this);
	}

	@Override
	public void create(User user) {
		UserEntity entity = UserMapper.toEntity(user);

		repository.save(entity.toBuilder()
							  .password(passwordEncoder.encode(entity.getPassword()))
							  .build());
	}

	@Override
	public User findById(long uniqueId) {
		return UserMapper.toDomain(repository.findById(uniqueId)
											 .orElseThrow(
												 () -> new IllegalArgumentException("User not found: " + uniqueId)));
	}

	@Override
	public User findByUserId(String userId) {
		return UserMapper.toDomain(repository.findByUserId(userId)
											 .orElseThrow(
												 () -> new IllegalArgumentException("User not found: " + userId)));
	}

	@Override
	public User findByUserIdAndStatus(String userId, UserStatus status) {
		return UserMapper.toDomain(repository.findByUserIdAndStatus(userId, status)
											 .orElseThrow(
												 () -> new IllegalArgumentException("User not found: " + userId)));
	}
}
