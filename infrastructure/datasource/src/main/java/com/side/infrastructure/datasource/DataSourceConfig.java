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
        hikariDataSource.setJdbcUrl(dataSourceProperties.getJdbcUrl());
        hikariDataSource.setUsername(dataSourceProperties.getUsername());
        hikariDataSource.setPassword(dataSourceProperties.getPassword());

        hikariDataSource.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        hikariDataSource.setMinimumIdle(dataSourceProperties.getMinimumIdle());
        hikariDataSource.setConnectionTestQuery("SELECT 1");
        hikariDataSource.setConnectionInitSql("SELECT 1");
        hikariDataSource.setAutoCommit(dataSourceProperties.isAutoCommit());
        hikariDataSource.setIsolateInternalQueries(dataSourceProperties.isIsolateInternalQueries());
        hikariDataSource.setTransactionIsolation(dataSourceProperties.getTransactionIsolate());
        hikariDataSource.setConnectionTimeout(dataSourceProperties.getConnectionTimeout());
        hikariDataSource.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        hikariDataSource.setMaxLifetime(dataSourceProperties.getMaxLifetime()); // 30분
        hikariDataSource.setLeakDetectionThreshold(dataSourceProperties.getLeakDetectionThreshold()); // 2초
        hikariDataSource.setPoolName(dataSourceProperties.getPoolName());

        return hikariDataSource;
    }
}
