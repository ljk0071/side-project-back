package com.side.infrastructure.valkey.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisKeyNames {
    public static final String JWT_REFRESH_TOKEN = "jwt:refresh-token:";

}
