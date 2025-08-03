package com.side.infrastructure.jpa.repository;

import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepository;
import com.side.domain.repository.NoticeRepositoryManager;
import com.side.infrastructure.jpa.entity.NoticeEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.side.domain.RepositoryTypeEnum.JPA;
import static com.side.infrastructure.jpa.mapper.NoticeMapper.NoticeMapper;

@Repository
@RequiredArgsConstructor
public class NoticeJpaRepository implements NoticeRepository {

    private final NoticeJpaInterface repository;

    @PostConstruct
    public void init() {
        NoticeRepositoryManager.addNoticeRepository(JPA, this);
    }

    @Override
    public void create(Notice notice) {

        NoticeEntity noticeEntity = NoticeMapper.toEntity(notice);

        repository.save(noticeEntity);
    }

    @Override
    public void bulkcreate(List<Notice> notices) {

    }

    @Override
    public List<Notice> find(String keyword, NoticeSearchType type) {

        throw new UnsupportedOperationException("사용 안함");
    }

    @Override
    public Optional<Notice> findById(Long id) {

        throw new UnsupportedOperationException("사용 안함");
    }

    @Override
    public void increaseViewCount(Long id) {
        repository.increaseViewCount(id);
    }

}
