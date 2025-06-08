package com.side.security.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.side.domain.memory.repository.MemoryRepository;

@Component
public class RoleHierarchyConfig {

	private static final String ROLE_HIERARCHY = "ROLE_HIERARCHY";

	@Bean
	public static RoleHierarchy roleHierarchy(MemoryRepository memoryRepository) {

		return RoleHierarchyImpl.fromHierarchy(
			memoryRepository.get(ROLE_HIERARCHY, new TypeReference<Map<String, String>>() {}).get("roleHierarchy"));
	}
}