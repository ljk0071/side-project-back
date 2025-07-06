package com.side.domain.service;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import com.side.domain.enums.NoticeSearchType;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.repository.NoticeRepositoryManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.side.domain.RepositoryTypeEnum.JOOQ;
import static com.side.domain.RepositoryTypeEnum.JPA;

@Service
public class NoticeService {

    public void create(Notice notice) {
        NoticeRepositoryManager.getNoticeRepository(JPA)
                               .create(initForCreate(notice));
    }

    public Notice initForCreate(Notice notice) {
        return notice.toBuilder()
                     .viewCount(0L)
                     .status(YesNoDeleteStatus.YES)
                     .metadata(Metadata.init())
                     .build();
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

    public List<Notice> find(String keyword, NoticeSearchType type) {

        return NoticeRepositoryManager.getNoticeRepository(JOOQ)
                                      .find(keyword, type);
    }

}
