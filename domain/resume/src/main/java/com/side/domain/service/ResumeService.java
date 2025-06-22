package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import org.springframework.stereotype.Service;

import com.side.domain.Metadata;
import com.side.domain.StatusTypeEnum;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeRepositoryManager;

@Service
public class ResumeService {

	public void create(Resume resume) {

		ResumeRepositoryManager.getDefaultResumeRepository()
							   .create(resume.toBuilder()
											 .status(StatusTypeEnum.Y)
											 .metadata(Metadata.builder().build())
											 .build());
	}

	public void delete(long resumeId) {
		ResumeRepositoryManager.getResumeRepository(JOOQ)
							   .delete(resumeId);
	}
}