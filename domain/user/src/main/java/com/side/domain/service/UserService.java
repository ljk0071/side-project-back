package com.side.domain.service;


import com.side.domain.Metadata;
import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.User;
import com.side.domain.model.UserDiscordAuth;
import com.side.domain.repository.UserReader;
import com.side.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserReader userReader;

    private final UserWriter userWriter;

    public Optional<User> findByUserId(String userId) {
        return userReader.findByUserId(userId);
    }

    public User getByUserId(String userId) {
        return userReader.findByUserId(userId)
                         .orElseThrow(() -> new NotExistException("찾을 수 없는 유저입니다."));
    }

    public Optional<User> findByDiscordId(String discordId) {
        return userReader.findByDiscordId(discordId);
    }

    public long create(User user) {
        return userWriter.create(initForCreate(user));
    }

    public long createFromDiscord(UserDiscordAuth userDiscordAuth) {
        return userWriter.createFromDiscord(initForCreate(userDiscordAuth));
    }

    public User loadUserByUserId(String userId) {
        return userReader.findByUserIdAndStatus(userId, UserStatus.ACTIVE);
    }

    private User initForCreate(User user) {
        return user.toBuilder()
                   .revision(0L)
                   .status(UserStatus.ACTIVE)
                   .type(UserType.NORMAL)
                   .metadata(Metadata.init())
                   .build();
    }

    private UserDiscordAuth initForCreate(UserDiscordAuth userDiscordAuth) {
        return userDiscordAuth.toBuilder()
                              .revision(0L)
                              .metadata(Metadata.init())
                              .build();
    }
}

