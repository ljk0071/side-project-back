package com.side.infrastructure.jpa.repository;

import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeRepository;
import com.side.domain.repository.ResumeRepositoryManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.side.domain.RepositoryTypeEnum.JPA;
import static com.side.infrastructure.jpa.mapper.ResumeMapper.ResumeMapper;

@RequiredArgsConstructor
@Repository
public class ResumeJpaRepository implements ResumeRepository {

    private final ResumeJpaInterface repository;

    @PostConstruct
    public void init() {
        ResumeRepositoryManager.addResumeRepository(JPA, this);
    }

    @Override
    public void create(Resume resume) {

        repository.save(ResumeMapper.toEntity(resume));
    }

    @Override
    public void delete(long resumeId) {
        throw new UnsupportedOperationException("jpa에서는 비효율적이므로 soft delete를 하지 않음");
    }
}