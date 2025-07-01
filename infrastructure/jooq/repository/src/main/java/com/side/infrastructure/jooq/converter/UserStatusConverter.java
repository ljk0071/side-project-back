package com.side.infrastructure.jooq.converter;

import com.side.domain.enums.UserStatus;
import org.jooq.Converter;

import java.util.Arrays;

public class UserStatusConverter implements Converter<String, UserStatus> {
    @Override
    public UserStatus from(String databaseObject) {
        if (databaseObject == null) return null;
        return Arrays.stream(UserStatus.values())
                     .filter(status -> status.getValue().equals(databaseObject))
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public String to(UserStatus userObject) {
        return userObject != null ? userObject.getValue() : null;
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<UserStatus> toType() {
        return UserStatus.class;
    }
}