package com.side.domain.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.side.domain.enums.RepositoryTypeEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRoleRepositoryManager {

	private static Map<RepositoryTypeEnum, UserRoleRepository> userRoleRepositoryMap = new ConcurrentHashMap<>();

	public static Map<RepositoryTypeEnum, UserRoleRepository> getUserRoleRepositoryMap() {
		return new ConcurrentHashMap<>(userRoleRepositoryMap);
	}

	public static void setUserRoleRepositoryMap(Map<RepositoryTypeEnum, UserRoleRepository> userRoleRepositoryMap) {
		UserRoleRepositoryManager.userRoleRepositoryMap =
			userRoleRepositoryMap != null ? new ConcurrentHashMap<>(userRoleRepositoryMap) : null;
	}

	public static UserRoleRepository getDefaultRoleRepository() {
		return userRoleRepositoryMap.get(JPA);
	}

	public static UserRoleRepository getUserRoleRepository(RepositoryTypeEnum repositoryType) {
		return userRoleRepositoryMap.get(repositoryType);
	}

	public static void addUserRoleRepository(RepositoryTypeEnum repositoryType, UserRoleRepository roleRepository) {
		userRoleRepositoryMap.put(repositoryType, roleRepository);
	}
}
