package com.side.domain.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.side.domain.enums.RepositoryTypeEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRepositoryManager {

	private static Map<RepositoryTypeEnum, UserRepository> userRepositoryMap = new ConcurrentHashMap<>();

	public static Map<RepositoryTypeEnum, UserRepository> getUserRepositoryMap() {
		return new ConcurrentHashMap<>(userRepositoryMap);
	}

	public static void setUserRepositoryMap(Map<RepositoryTypeEnum, UserRepository> userRepositoryMap) {
		UserRepositoryManager.userRepositoryMap =
			userRepositoryMap != null ? new ConcurrentHashMap<>(userRepositoryMap) : null;
	}

	public static UserRepository getDefaultUserRepository() {
		return userRepositoryMap.get(JPA);
	}

	public static UserRepository getUserRepository(RepositoryTypeEnum repositoryType) {
		return userRepositoryMap.get(repositoryType);
	}

	public static void addUserRepository(RepositoryTypeEnum repositoryType, UserRepository noticeRepository) {
		userRepositoryMap.put(repositoryType, noticeRepository);
	}
}
