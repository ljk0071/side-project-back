package com.side.infrastructure.jooq.config;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration(proxyBeanMethods = false)
public class JooqConfig {

	@Bean
	public DSLContext dslContext(DataSource dataSource) {

		DefaultConfiguration configuration = new DefaultConfiguration();

		// 데이터베이스 연결
		configuration.setDataSource(new TransactionAwareDataSourceProxy(dataSource));
		// 사용하는 SQL Dialect (e.g., MYSQL, POSTGRES)
		configuration.setSQLDialect(SQLDialect.MYSQL);

		Settings settings = new Settings()
								.withRenderSchema(false) // 스키마 이름 출력 생략
								.withExecuteWithOptimisticLocking(true) // 낙관적 락 사용
								.withRenderFormatted(true); // 쿼리를 포맷된 형태로 출력

		configuration.setSettings(settings);

		return DSL.using(configuration);
	}
}
