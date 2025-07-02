package com.side.infrastructure.valkey.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("redis")
public class RedisProperties {

    private String connectionIp;
    private String connectionPort;
    private String password;
    private long timeout;

}