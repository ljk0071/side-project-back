package com.side.infrastructure.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {

    @Bean
    public HikariDataSource dataSource(DataSourceProperties dataSourceProperties) {

        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl(dataSourceProperties.jdbcUrl());
        hikariDataSource.setUsername(dataSourceProperties.username());
        hikariDataSource.setPassword(dataSourceProperties.password());

        hikariDataSource.setMaximumPoolSize(dataSourceProperties.maximumPoolSize());
        hikariDataSource.setMinimumIdle(dataSourceProperties.minimumIdle());
        hikariDataSource.setConnectionTestQuery("SELECT 1");
        hikariDataSource.setConnectionInitSql("SELECT 1");
        hikariDataSource.setAutoCommit(dataSourceProperties.autoCommit());
        hikariDataSource.setIsolateInternalQueries(dataSourceProperties.isolateInternalQueries());
        hikariDataSource.setTransactionIsolation(dataSourceProperties.transactionIsolate());
        hikariDataSource.setConnectionTimeout(dataSourceProperties.connectionTimeout());
        hikariDataSource.setIdleTimeout(dataSourceProperties.idleTimeout());
        hikariDataSource.setMaxLifetime(dataSourceProperties.maxLifetime()); // 30분
        hikariDataSource.setLeakDetectionThreshold(dataSourceProperties.leakDetectionThreshold()); // 2초
        hikariDataSource.setPoolName(dataSourceProperties.poolName());

        return hikariDataSource;
    }
}
