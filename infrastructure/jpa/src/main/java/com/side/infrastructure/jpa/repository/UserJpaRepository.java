package com.side.infrastructure.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.side.domain.enums.UserStatus;
import com.side.infrastructure.jpa.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserIdAndStatus(String userId, UserStatus status);

	Optional<UserEntity> findByUserId(String userId);
}
