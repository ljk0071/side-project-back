package com.side.domain.repository;

import com.side.domain.model.Notice;

import java.util.List;

public interface NoticeRepository {

    void create(Notice notice);

    void bulkcreate(List<Notice> notices);

    Notice find(Notice notice);
}

