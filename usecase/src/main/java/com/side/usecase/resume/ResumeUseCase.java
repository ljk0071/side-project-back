package com.side.usecase.resume;

import com.side.domain.model.Resume;
import com.side.domain.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /**
     * 사용자 고유 ID로 이력서를 조회합니다.
     *
     * @param userUniqueId 사용자 고유 ID
     * @return 이력서 Optional 객체
     */
    @Transactional(readOnly = true)
    public Optional<Resume> findByUserUniqueId(Long userUniqueId) {
        return resumeService.findByUserUniqueId(userUniqueId);
    }
}