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

    /**
     * 주어진 이력서를 생성합니다.
     *
     * @param resume 생성할 이력서 객체
     * @return 생성된 이력서의 ID
     */
    @Transactional
    public long create(Resume resume) {
        return resumeService.create(resume);
    }
}