package com.side.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.side.infrastructure.jpa.entity.ResumeEntity;

@Repository
public interface ResumeJpaInterface extends JpaRepository<ResumeEntity, Long> {
}