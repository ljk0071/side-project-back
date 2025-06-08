package com.side.infrastructure.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {

	private int getCpuCores() {
		return Runtime.getRuntime().availableProcessors();
	}

	private int getEffectiveDisks() {
		return 2; // Example fixed value, should be configured based on actual system
	}

	private int calculateOptimalPoolSize() {
		return (getCpuCores() * 2) + getEffectiveDisks();
	}

	@Bean
	public HikariDataSource dataSource(DataSourceProperties dataSourceProperties) {

		HikariDataSource hikariDataSource = new HikariDataSource();

		hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariDataSource.setJdbcUrl(dataSourceProperties.getJdbcUrl());
		hikariDataSource.setUsername(dataSourceProperties.getUsername());
		hikariDataSource.setPassword(dataSourceProperties.getPassword());

		int maximumPoolSize = calculateOptimalPoolSize();

		dataSourceProperties.setMaximumPoolSize(maximumPoolSize);

		dataSourceProperties.setMinimumIdle(Math.max(maximumPoolSize / 3, 1));

		hikariDataSource.setMaximumPoolSize(maximumPoolSize);
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
