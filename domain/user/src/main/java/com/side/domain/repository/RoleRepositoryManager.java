package com.side.domain.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.side.domain.enums.RepositoryTypeEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleRepositoryManager {

	private static Map<RepositoryTypeEnum, RoleRepository> roleRepositoryMap = new ConcurrentHashMap<>();

	public static Map<RepositoryTypeEnum, RoleRepository> getRoleRepositoryMap() {
		return new ConcurrentHashMap<>(roleRepositoryMap);
	}

	public static void setRoleRepositoryMap(Map<RepositoryTypeEnum, RoleRepository> roleRepositoryMap) {
		RoleRepositoryManager.roleRepositoryMap =
			roleRepositoryMap != null ? new ConcurrentHashMap<>(roleRepositoryMap) : null;
	}

	public static RoleRepository getDefaultRoleRepository() {
		return roleRepositoryMap.get(JPA);
	}

	public static RoleRepository getRoleRepository(RepositoryTypeEnum repositoryType) {
		return roleRepositoryMap.get(repositoryType);
	}

	public static void addRoleRepository(RepositoryTypeEnum repositoryType, RoleRepository roleRepository) {
		roleRepositoryMap.put(repositoryType, roleRepository);
	}
}
