package com.side.domain.repository;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.User;

import java.util.Optional;

public interface UserReader {

    User findById(long uniqueId);

    Optional<User> findByUserId(String userId);

    Optional<User> findByDiscordId(String discordId);

    User findByUserIdAndStatus(String userId, UserStatus status);
}

