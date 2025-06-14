package com.side.domain.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.side.domain.enums.RepositoryTypeEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartyRecruitRepositoryManager {

	private static Map<RepositoryTypeEnum, PartyRecruitRepository> partyRecruitRepositoryMap = new ConcurrentHashMap<>();

	public static Map<RepositoryTypeEnum, PartyRecruitRepository> getPartyRecruitRepositoryMap() {
		return new ConcurrentHashMap<>(partyRecruitRepositoryMap);
	}

	public static void setPartyRecruitRepositoryMap(
		Map<RepositoryTypeEnum, PartyRecruitRepository> partyRecruitRepositoryMap) {
		PartyRecruitRepositoryManager.partyRecruitRepositoryMap =
			partyRecruitRepositoryMap != null ? new ConcurrentHashMap<>(partyRecruitRepositoryMap) : null;
	}

	public static PartyRecruitRepository getDefaultPartyRecruitRepository() {
		return partyRecruitRepositoryMap.get(JPA);
	}

	public static PartyRecruitRepository getPartyRecruitRepository(RepositoryTypeEnum repositoryType) {
		return partyRecruitRepositoryMap.get(repositoryType);
	}

	public static void addPartyRecruitRepository(RepositoryTypeEnum repositoryType,
		PartyRecruitRepository partyRecruitRepository) {
		partyRecruitRepositoryMap.put(repositoryType, partyRecruitRepository);
	}
}
