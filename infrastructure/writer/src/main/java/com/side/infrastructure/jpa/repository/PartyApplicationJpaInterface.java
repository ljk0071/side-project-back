package com.side.infrastructure.jpa.repository;

import com.side.infrastructure.jpa.entity.PartyApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyApplicationJpaInterface extends JpaRepository<PartyApplicationEntity, Long> {
}