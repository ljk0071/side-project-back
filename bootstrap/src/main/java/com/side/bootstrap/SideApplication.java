package com.side.bootstrap;

import com.side.infrastructure.datasource.DataSourceProperties;
import com.side.infrastructure.valkey.properties.RedisProperties;
import com.side.security.config.DiscordOAuth2Properties;
import com.side.security.jwt.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.TimeZone;

@ConfigurationPropertiesScan(basePackageClasses = {
        JwtProperties.class,
        RedisProperties.class,
        DataSourceProperties.class,
        DiscordOAuth2Properties.class})
@SpringBootApplication(scanBasePackages = {
        "com.side.security.config",
        "com.side.security.service",
        "com.side.security.util",
        "com.side.security.filter",
        "com.side.security.jwt.config",
        "com.side.security.jwt.filter",
        "com.side.security.jwt.service",
        "com.side.rest.advice",
        "com.side.rest.config",
        "com.side.rest.login",
        "com.side.rest.domain.*.controller",
        "com.side.rest.external.config",
        "com.side.rest.util",
        "com.side.websocket.config",
        "com.side.websocket.controller",
        "com.side.websocket.service",
        "com.side.usecase",
        "com.side.domain.service",
        "com.side.domain.memory",
        "com.side.domain.config",
        "com.side.infrastructure.datasource",
        "com.side.infrastructure.jooq.config",
        "com.side.infrastructure.jooq.repository",
        "com.side.infrastructure.jpa.config",
        "com.side.infrastructure.jpa.converter",
        "com.side.infrastructure.jpa.repository",
        "com.side.infrastructure.jdbc",
        "com.side.infrastructure.valkey.cache.config",
        "com.side.infrastructure.valkey.cache.service",
        "com.side.infrastructure.valkey.config",
        "com.side.infrastructure.valkey.properties",
        "com.side.infrastructure.valkey.repository",
})
public class SideApplication {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(SideApplication.class, args);
    }
}