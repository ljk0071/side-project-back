package com.side.domain.repository;

import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Notice;

import java.util.List;

public interface NoticeRepository {

    void create(Notice notice);

    void bulkcreate(List<Notice> notices);

    List<Notice> find(String keyword, NoticeSearchType type);

}

