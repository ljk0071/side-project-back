package com.side.infrastructure.jpa.repository;

import com.side.infrastructure.jpa.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeJpaInterface extends JpaRepository<NoticeEntity, Long> {
}
