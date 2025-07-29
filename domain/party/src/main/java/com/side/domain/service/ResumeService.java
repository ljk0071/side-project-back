package com.side.domain.service;


import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
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

    public Resume initForCreate(Resume resume) {
        return resume.toBuilder()
                     .status(YesNoDeleteStatus.YES)
                     .metadata(Metadata.init())
                     .build();
    }
}