package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import org.springframework.stereotype.Service;

import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepositoryManager;

@Service
public class NoticeService {

	public Notice find(Notice notice) {
		return NoticeRepositoryManager.getNoticeRepository(JPA)
									  .find(notice);
	}
}
