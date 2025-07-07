package com.side.usecase.board;

import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Notice;
import com.side.domain.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
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

    @Transactional
    public List<Notice> find(String keyword, NoticeSearchType noticeSearchType) {

        return noticeService.find(keyword, noticeSearchType);
    }

    @Transactional
    public Notice findById(Long id) {


        Notice notice = noticeService.findById(id)
                                     .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));

        CompletableFuture.runAsync(() -> {
            noticeService.increaseViewCount(id);
        }).exceptionally(ex -> {
            log.error("조회수 증가 실패: ", ex);
            return null;
        });

        return notice;
    }


}
