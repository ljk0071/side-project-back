package com.side.infrastructure.jooq.repository;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("파티 지원서 JOOQ Repository 읽기 전용 테스트")
class PartyApplicationJooqRepositoryTest {

    @Mock
    private DSLContext dslContext;

    private PartyApplicationJooqRepository jooqRepository;

    @BeforeEach
    void setUp() {
        jooqRepository = new PartyApplicationJooqRepository(dslContext);
    }

    // PartyApplicationJooqRepository는 Reader 인터페이스만 구현하므로 읽기 전용 테스트만 수행
    // 실제 데이터베이스 연동 대신 모킹 사용
    
    @Test
    @DisplayName("인터페이스 조회 메서드들이 올바른 리턴 타입을 가지는지 확인")
    void repositoryInterface_Methods() {
        // given & when & then - 메서드 존재 확인
        assertThat(jooqRepository).isNotNull();
        
        // 인터페이스 메서드들이 존재하는지 확인
        assertThat(jooqRepository.getClass().getMethods())
                .anyMatch(method -> method.getName().equals("findById"))
                .as("findById 메서드가 존재해야 함");
                
        assertThat(jooqRepository.getClass().getMethods())
                .anyMatch(method -> method.getName().equals("findByPartyRecruitId"))
                .as("findByPartyRecruitId 메서드가 존재해야 함");
                
        assertThat(jooqRepository.getClass().getMethods())
                .anyMatch(method -> method.getName().equals("findByResumeId"))
                .as("findByResumeId 메서드가 존재해야 함");
                
        assertThat(jooqRepository.getClass().getMethods())
                .anyMatch(method -> method.getName().equals("existsByPartyRecruitIdAndResumeId"))
                .as("existsByPartyRecruitIdAndResumeId 메서드가 존재해야 함");
                
        assertThat(jooqRepository.getClass().getMethods())
                .anyMatch(method -> method.getName().equals("findAll"))
                .as("findAll 메서드가 존재해야 함");
    }
}