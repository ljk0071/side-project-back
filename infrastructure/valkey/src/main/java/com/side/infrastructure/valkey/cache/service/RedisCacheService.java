package com.side.infrastructure.valkey.cache.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.side.infrastructure.valkey.cache.constants.RedisCacheNames;

@Service
public class RedisCacheService {

	@CacheEvict(value = RedisCacheNames.USER_ROLES, key = "#userId")
	public void evictUserRolesCache(String userId) {
		// userId로 role 캐시삭제
	}

	@CacheEvict(value = RedisCacheNames.ROLE_HIERARCHY, allEntries = true)
	public void evcitRoleHierarchyCache() {
		//rolehierarchy 초기화
	}
}
