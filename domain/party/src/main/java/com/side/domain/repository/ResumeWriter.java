package com.side.domain.repository;

import com.side.domain.model.Resume;

public interface ResumeWriter {

    long create(Resume resume);
}