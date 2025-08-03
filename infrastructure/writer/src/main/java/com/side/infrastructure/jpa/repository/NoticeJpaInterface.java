package com.side.infrastructure.jpa.repository;

import com.side.infrastructure.jpa.entity.NoticeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeJpaInterface extends JpaRepository<NoticeEntity, Long> {

    @Modifying
    @Transactional
    @Query("update NoticeEntity n set n.viewCount = n.viewCount + 1 where n.id = :id")
    void increaseViewCount(Long id);
}
