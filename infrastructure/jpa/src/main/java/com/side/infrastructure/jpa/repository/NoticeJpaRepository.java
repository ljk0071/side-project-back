package com.side.infrastructure.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.side.infrastructure.jpa.entity.NoticeEntity;

@Repository
public interface NoticeJpaRepository extends JpaRepository<NoticeEntity, Long> {
	Optional<NoticeEntity> findByArticle_Title(String name);
}
