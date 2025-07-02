package com.side.infrastructure.datasource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("mysql")
@ToString
public class DataSourceProperties {

    private String jdbcUrl;
    private String username;
    private String password;
    private int maximumPoolSize;
    private int minimumIdle;
    private boolean autoCommit;
    private boolean isolateInternalQueries;
    private String transactionIsolate;
    private int connectionTimeout;
    private int idleTimeout;
    private int maxLifetime;
    private int leakDetectionThreshold;
    private String poolName;
}
