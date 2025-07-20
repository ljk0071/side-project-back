package com.side.infrastructure.jpa.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static com.side.security.service.SecurityHelper.getAuthenticatedUser;
import static com.side.security.service.SecurityHelper.isAuthenticated;

@EnableJpaAuditing
@Configuration(proxyBeanMethods = false)
public class AuditorAwareConfig implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {

        return isAuthenticated() ? getAuthenticatedUser().uniqueId().describeConstable() : Optional.empty();
    }
}
