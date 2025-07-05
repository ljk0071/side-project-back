package com.side.domain.service;


import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResumeService {

    private final ResumeWriter resumeWriter;

    public long create(Resume resume) {

        return resumeWriter.create(initForCreate(resume));
    }

    public Resume initForCreate(Resume resume) {
        return resume.toBuilder()
                     .status(YesNoDeleteStatus.YES)
                     .metadata(Metadata.init())
                     .build();
    }
}