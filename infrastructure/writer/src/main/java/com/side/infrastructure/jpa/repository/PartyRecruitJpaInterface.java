package com.side.infrastructure.jpa.repository;

import com.side.infrastructure.jpa.entity.PartyRecruitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRecruitJpaInterface extends JpaRepository<PartyRecruitEntity, Long> {
}
