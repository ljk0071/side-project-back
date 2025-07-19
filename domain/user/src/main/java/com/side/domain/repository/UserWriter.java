package com.side.domain.repository;

import com.side.domain.model.User;
import com.side.domain.model.UserDiscordAuth;

public interface UserWriter {

    long create(User user);

    long createFromDiscord(UserDiscordAuth userDiscordAuth);

}

