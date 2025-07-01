package com.side.domain.repository;

import com.side.domain.model.Resume;

public interface ResumeRepository {
    void create(Resume resume);

    void delete(long resumeId);
}