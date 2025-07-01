package com.side.usecase.resume;

import com.side.domain.model.Resume;
import com.side.domain.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeUseCase {

    private final ResumeService resumeService;

    @Transactional
    public void create(Resume resume) {
        resumeService.create(resume);
    }

    @Transactional
    public void delete(long resumeId) {
        resumeService.delete(resumeId);
    }
}