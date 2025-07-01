package com.side.infrastructure.jpa.repository;

import com.side.infrastructure.jpa.entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeJpaInterface extends JpaRepository<ResumeEntity, Long> {
}