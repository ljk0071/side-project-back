package com.side.rest.board.controller;

import static com.side.rest.mapper.NoticeMapper.*;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/test")
	public ResponseEntity<Object> getAuthToken() {
		return ResponseEntity.ok(Map.of("hello", "world"));
	}

	@PostMapping("/test")
	public ResponseEntity<Object> getAuthToken2() {
		return ResponseEntity.ok(Map.of("hello", "world2"));
	}

	// @PostMapping
	// public ResponseEntity<Void> create(@RequestBody(required = false) NoticeRequestDto noticeRequestDto) {
	//
	// 	noticeUseCase.create(Notice.builder()
	// 										  .article(Article.builder()
	// 														  .title("Test Title")
	// 														  .contents("Test Contents")
	// 														  .build())
	// 										  .build());
	//
	// 	// noticeUseCase.create(NoticeMapper.toDomain(noticeRequestDto));
	//
	// 	return ResponseEntity.ok().build();
	// }

	@GetMapping
	public ResponseEntity<NoticeResponseDto> get(NoticeRequestDto noticeRequestDto) {

		return ResponseEntity.ok(NoticeMapper.toResponse(noticeUseCase.find(NoticeMapper.toDomain(noticeRequestDto))));
	}
}
