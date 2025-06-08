package com.side.domain.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.side.domain.enums.RepositoryTypeEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleHierarchyRepositoryManager {

	private static Map<RepositoryTypeEnum, RoleHierarchyRepository> roleHierarchyRepositoryMap = new ConcurrentHashMap<>();

	public static Map<RepositoryTypeEnum, RoleHierarchyRepository> getRoleHierarchyRepositoryMap() {
		return new ConcurrentHashMap<>(roleHierarchyRepositoryMap);
	}

	public static void setRoleHierarchyRepositoryMap(
		Map<RepositoryTypeEnum, RoleHierarchyRepository> roleHierarchyRepositoryMap) {
		RoleHierarchyRepositoryManager.roleHierarchyRepositoryMap =
			roleHierarchyRepositoryMap != null ? new ConcurrentHashMap<>(roleHierarchyRepositoryMap) : null;
	}

	public static RoleHierarchyRepository getDefaultRoleHierarchyRepository() {
		return roleHierarchyRepositoryMap.get(JPA);
	}

	public static RoleHierarchyRepository getRoleHierarchyRepository(RepositoryTypeEnum repositoryType) {
		return roleHierarchyRepositoryMap.get(repositoryType);
	}

	public static void addRoleHierarchyRepository(RepositoryTypeEnum repositoryType,
		RoleHierarchyRepository roleHierarchyRepository) {
		roleHierarchyRepositoryMap.put(repositoryType, roleHierarchyRepository);
	}
}
