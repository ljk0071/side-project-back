package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.JOOQ;
import static com.side.domain.enums.RepositoryTypeEnum.JPA;

import com.side.domain.Metadata;
import com.side.domain.model.Article;
import com.side.domain.model.Notice;
import com.side.domain.model.UserReaction;
import com.side.domain.repository.NoticeRepositoryFactory;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    public void create(Notice notice) {
        NoticeRepositoryFactory.getNoticeRepository(JPA)
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

        NoticeRepositoryFactory.getNoticeRepository(JOOQ)
                .bulkcreate(notices.stream()
                        .map(notice -> notice.toBuilder()
                                .article(notice.article().toBuilder()
                                        .userReaction(
                                                UserReaction
                                                        .builder()
                                                        .likes(0)
                                                        .dislikes(0)
                                                        .build())
                                        .build())
                                .metadata(Metadata.builder()
                                        .createdBy(1)
                                        .createdAt(Instant.now())
                                        .build())
                                .build())
                        .toList());

    }

    public Notice find(Notice notice) {
        return NoticeRepositoryFactory.getNoticeRepository(JPA)
                .find(notice);
    }
}
