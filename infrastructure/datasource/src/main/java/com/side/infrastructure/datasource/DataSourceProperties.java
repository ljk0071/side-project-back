package com.side.infrastructure.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mysql")
public record DataSourceProperties(
        String jdbcUrl,
        String username,
        String password,
        int maximumPoolSize,
        int minimumIdle,
        boolean autoCommit,
        boolean isolateInternalQueries,
        String transactionIsolate,
        int connectionTimeout,
        int idleTimeout,
        int maxLifetime,
        int leakDetectionThreshold,
        String poolName
) {
}
