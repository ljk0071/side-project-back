package com.side.infrastructure.jpa.repository;

import com.side.domain.model.Resume;
import com.side.domain.repository.ResumeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.side.infrastructure.jpa.mapper.ResumeMapper.ResumeMapper;

@RequiredArgsConstructor
@Repository
public class ResumeJpaRepository implements ResumeWriter {

    private final ResumeJpaInterface repository;

    @Override
    public long create(Resume resume) {

        return repository.save(ResumeMapper.toEntity(resume)).getId();
    }
}