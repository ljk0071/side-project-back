package com.side.domain.repository;

import java.util.List;

import com.side.domain.model.Notice;

public interface NoticeRepository {

	void create(Notice notice);

	void bulkcreate(List<Notice> notices);

	Notice find(Notice notice);
}

