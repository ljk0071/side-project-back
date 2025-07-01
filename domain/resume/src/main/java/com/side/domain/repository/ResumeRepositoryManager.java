package com.side.domain.repository;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.side.domain.RepositoryTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.side.domain.RepositoryTypeEnum.JPA;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResumeRepositoryManager {

    private static Map<RepositoryTypeEnum, ResumeRepository> resumeRepositoryMap = new ConcurrentHashMap<>();

    public static Map<RepositoryTypeEnum, ResumeRepository> getResumeRepositoryMap() {
        return new ConcurrentHashMap<>(resumeRepositoryMap);
    }

    public static void setResumeRepositoryMap(
            Map<RepositoryTypeEnum, ResumeRepository> resumeRepositoryMap) {
        ResumeRepositoryManager.resumeRepositoryMap =
                resumeRepositoryMap != null ? new ConcurrentHashMap<>(resumeRepositoryMap) : null;
    }

    public static ResumeRepository getDefaultResumeRepository() {
        return resumeRepositoryMap.get(JPA);
    }

    public static ResumeRepository getResumeRepository(RepositoryTypeEnum repositoryType) {
        return resumeRepositoryMap.get(repositoryType);
    }

    public static void addResumeRepository(RepositoryTypeEnum repositoryType,
                                           ResumeRepository resumeRepository) {
        resumeRepositoryMap.put(repositoryType, resumeRepository);
    }
}