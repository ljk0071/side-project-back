package com.side.infrastructure.valkey.cache.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisCacheNames {
    private static final String Cache = "cache:";
    public static final String USER_STATUS = Cache + "user:status";
    public static final String USER_ROLES = Cache + "user:roles";
    public static final String ROLE_HIERARCHY = Cache + "role:hierarchy";
}