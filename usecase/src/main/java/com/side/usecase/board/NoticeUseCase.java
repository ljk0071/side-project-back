package com.side.usecase.board;

import org.springframework.stereotype.Service;

import com.side.domain.model.Notice;
import com.side.domain.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeUseCase {

	private final NoticeService noticeService;

	public Notice find(Notice notice) {

		return noticeService.find(notice);
	}

}
