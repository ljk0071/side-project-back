package com.side.usecase.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.side.domain.model.Notice;
import com.side.domain.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeUseCase {

	private final NoticeService noticeService;

	@Transactional
	public void create(Notice notice) {
		noticeService.create(notice);
	}

	@Transactional
	public void bulkCreate(List<Notice> notices) {

		noticeService.bulkCreate(notices);
	}

	public Notice find(Notice notice) {

		return noticeService.find(notice);
	}

}
