package com.side.domain.service;

import static com.side.domain.RepositoryTypeEnum.JOOQ;
import static com.side.domain.RepositoryTypeEnum.JPA;

import com.side.domain.Metadata;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.model.UserReaction;
import com.side.domain.repository.NoticeRepositoryManager;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    public void create(Notice notice) {
        NoticeRepositoryManager.getNoticeRepository(JPA)
                               .create(notice);
    }

    public void bulkCreate(List<Notice> notices) {

        for (int i = 0; i < 10_000; i++) {
            notices.add(Notice.builder()
                              .article(Article.builder()
                                              .title(String.valueOf(i))
                                              .contents("asd" + i)
                                              .build())
                              .build());
        }

        NoticeRepositoryManager.getNoticeRepository(JOOQ)
                               .bulkcreate(notices.stream()
                                                  .map(notice -> notice.toBuilder()
                                                                       .metadata(Metadata.builder()
                                                                                         .createdBy(1)
                                                                                         .createdAt(Instant.now())
                                                                                         .build())
                                                                       .build())
                                                  .toList());

    }

    public Notice find(Notice notice) {
        return NoticeRepositoryManager.getNoticeRepository(JPA)
                                      .find(notice);
    }
}
