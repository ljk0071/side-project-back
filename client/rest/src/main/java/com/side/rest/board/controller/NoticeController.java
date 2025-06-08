package com.side.rest.board.controller;

import static com.side.rest.mapper.NoticeMapper.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.rest.board.dto.request.NoticeRequestDto;
import com.side.rest.board.dto.response.NoticeResponseDto;
import com.side.usecase.board.NoticeUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeUseCase noticeUseCase;

	@GetMapping
	public ResponseEntity<NoticeResponseDto> get(NoticeRequestDto noticeRequestDto) {

		return ResponseEntity.ok(NoticeMapper.toResponse(noticeUseCase.find(NoticeMapper.toDomain(noticeRequestDto))));
	}
}
