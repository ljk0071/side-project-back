package com.side.infrastructure.jpa.config;

import static com.side.security.service.SecurityHelper.*;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.NonNull;

@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
public class AuditorAwareConfig implements AuditorAware<Long> {

	@Override
	@NonNull
	public Optional<Long> getCurrentAuditor() {

		return isAuthenticated() ? getAuthenticatedUser().uniqueId().describeConstable() : Optional.empty();
	}
}
