package com.side.domain.repository;

import com.side.domain.model.Resume;

import java.util.Optional;

public interface ResumeReader {

    Optional<Resume> findByUserUniqueId(Long userUniqueId);
}