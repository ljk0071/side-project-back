package com.side.domain.service;


import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeReader;
import com.side.domain.repository.ResumeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ResumeService {

    private final ResumeWriter resumeWriter;
    private final ResumeReader resumeReader;

    public long create(Resume resume) {

        return resumeWriter.create(initForCreate(resume));
    }

    public Optional<Resume> findByUserUniqueId(Long userUniqueId) {
        return resumeReader.findByUserUniqueId(userUniqueId);
    }

    public Optional<Resume> findByResumeId(long resumeId) {
        return resumeReader.findByResumeId(resumeId);
    }

    public Resume getByResumeId(long resumeId) {
        return resumeReader.findByResumeId(resumeId)
                           .orElseThrow(() -> new NotExistException(String.format("존재하지 않는 이력서 입니다: %d", resumeId)));
    }

    public Resume getByApplicationId(long partyApplicationId) {
        return resumeReader.findByApplicationId(partyApplicationId)
                           .orElseThrow(() -> new NotExistException(String.format("존재하지 않는 이력서 입니다: partyApplicationId: %d", partyApplicationId)));
    }

    public Resume initForCreate(Resume resume) {
        return resume.toBuilder()
                     .status(YesNoDeleteStatus.YES)
                     .metadata(Metadata.init())
                     .build();
    }
}