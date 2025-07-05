package com.side.rest.domain.board.controller;

import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Notice;
import com.side.rest.domain.board.dto.request.NoticeRequestDto;
import com.side.rest.domain.board.dto.response.NoticeResponseDto;
import com.side.usecase.board.NoticeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.side.rest.mapper.NoticeMapper.NoticeMapper;

@Slf4j
@RestController
@RequestMapping("/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeUseCase noticeUseCase;


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
    public ResponseEntity<List<NoticeResponseDto>> findNotice(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "all") NoticeSearchType type
    ) {
        List<Notice> notices = noticeUseCase.find(keyword, type);

        List<NoticeResponseDto> responsesList = notices.stream()
                                                       .map(NoticeMapper::toResponse)
                                                       .collect(Collectors.toList());

        return ResponseEntity.ok(responsesList);
    }


    @PostMapping
    public ResponseEntity<String> createNotice(@RequestBody NoticeRequestDto noticeRequestDto) {

        Notice notice = NoticeMapper.toDomain(noticeRequestDto);

        noticeUseCase.create(notice);

        return ResponseEntity.ok("게시글을 성공적으로 작성했습니다");
    }


}
