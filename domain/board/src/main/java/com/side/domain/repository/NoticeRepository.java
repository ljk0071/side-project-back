package com.side.domain.repository;

import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {

    void create(Notice notice);

    void bulkcreate(List<Notice> notices);

    List<Notice> find(String keyword, NoticeSearchType type);

    Optional<Notice> findById(Long id);

    void increaseViewCount(Long id);
}

