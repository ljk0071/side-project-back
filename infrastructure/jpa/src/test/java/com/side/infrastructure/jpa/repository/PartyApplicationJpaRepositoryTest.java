package com.side.infrastructure.jpa.repository;

import com.side.bootstrap.SideApplication;
import com.side.domain.model.*;
import com.side.domain.service.PartyApplicationService;
import com.side.domain.service.PartyRecruitService;
import com.side.domain.service.ResumeService;
import com.side.infrastructure.jooq.repository.PartyApplicationJooqRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = SideApplication.class)
@DisplayName("파티 지원서 Writer 테스트")
@Transactional
class PartyApplicationJpaRepositoryTest {

    @Autowired
    private PartyApplicationJpaRepository writer;

    @Autowired
    private PartyApplicationJooqRepository reader;

    @Autowired
    private PartyRecruitService recruitService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private PartyApplicationService service;

    private Long savedPartyRecruitId;
    private Long savedResumeId;
    private Long userId;

    @BeforeEach
    void setUp() {

        // 테스트용 데이터 ID 설정
        savedPartyRecruitId = recruitService.create(recruitService.initForCreate(PartyRecruit.builder()
                                                                                             .article(Article.builder()
                                                                                                             .title("\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 불어전  /불어전 6인 /불어전 6 /불어전6 인팟!!! \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25")
                                                                                                             .contents("┏━━━━━━━━━━━━┓ ┏━━━━━━━━━━━━┓\n" +
                                                                                                                     "   \uD83D\uDFE2좌3: 88허밋(140)  \uD83D\uDFE2우3:   87 허밋 (150)\n" +
                                                                                                                     "   \uD83D\uDD34좌3: 구인중(140)   \uD83D\uDFE2우3:  95 시마 (150)\n" +
                                                                                                                     " ┗━━━━━━━━━━━━┛ ┗━━━━━━━━━━━━┛\n" +
                                                                                                                     "      \uD83E\uDDD9\u200D  132 앜메            \uD83E\uDDD9\u200D♀\uFE0F  132비숍 (메용9) 답없구")
                                                                                                             .build())
                                                                                             .userUniqueId(1L)
                                                                                             .maxMembers(2)
                                                                                             .build()));

        savedResumeId = resumeService.create(resumeService.initForCreate(Resume.builder()
                                                                               .contents("테스트 이력서입니다.")
                                                                               .userUniqueId(1L)
                                                                               .build()));
        userId = 1L;

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                                                                                                         .uniqueId(userId)
                                                                                                         .build(), null));
    }

    @Test
    @DisplayName("파티 지원서 생성 - 성공")
    void create_Success() {
        // given
        PartyApplication application = service.initForCreate(savedPartyRecruitId, savedResumeId);

        // when
        long createdId = writer.create(application);
        System.out.println("partyRecruitId::::::: " + savedPartyRecruitId);
        System.out.println("resumeId::::::: " + savedResumeId);

        PartyApplication createdApplication = reader.findById(createdId).orElseThrow();
        System.out.println("createdPartyRecruitId::::::: " + createdApplication.partyRecruitId());
        System.out.println("createdResumeId::::::: " + createdApplication.resumeId());

        // then
        assertThat(createdApplication.partyRecruitId()).isEqualTo(application.partyRecruitId());
        assertThat(createdApplication.resumeId()).isEqualTo(application.resumeId());
        assertThat(createdApplication.metadata().createdBy()).isEqualTo(userId);
    }

    @Test
    @DisplayName("파티 지원서 생성 - 중복 지원 방지")
    void create_DuplicateApplication_ShouldThrowException() {
        // given
        PartyApplication application1 = service.initForCreate(savedPartyRecruitId, savedResumeId);

        writer.create(application1);

        PartyApplication application2 = service.initForCreate(savedPartyRecruitId, savedResumeId);

        // when & then
        assertThatThrownBy(() -> writer.create(application2))
                .as("동일한 파티에 동일한 이력서로 중복시 unique key에러")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}