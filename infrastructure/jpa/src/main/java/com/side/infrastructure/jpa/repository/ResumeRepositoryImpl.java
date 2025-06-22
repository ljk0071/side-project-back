package com.side.infrastructure.jpa.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jpa.mapper.ResumeMapper.*;

import org.springframework.stereotype.Repository;

import com.side.domain.enums.BoardStatusTypeEnum;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeRepository;
import com.side.domain.repository.ResumeRepositoryManager;
import com.side.infrastructure.jpa.entity.ResumeEntity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ResumeRepositoryImpl implements ResumeRepository {

	private final ResumeJpaRepository repository;

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