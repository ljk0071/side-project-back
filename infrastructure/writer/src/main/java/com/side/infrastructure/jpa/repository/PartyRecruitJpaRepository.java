package com.side.infrastructure.jpa.repository;

import com.side.domain.exception.NotExistException;
import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitWriter;
import com.side.infrastructure.jpa.entity.PartyRecruitEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.side.infrastructure.jpa.mapper.PartyRecruitMapper.PartyRecruitMapper;

@RequiredArgsConstructor
@Repository
public class PartyRecruitJpaRepository implements PartyRecruitWriter {

    private final PartyRecruitJpaInterface repository;

    @Override
    public long create(PartyRecruit partyRecruit) {

        return repository.save(PartyRecruitMapper.toEntity(partyRecruit)).getId();
    }

    @Override
    public int deleteRecruit(long partyRecruitId) {
        PartyRecruitEntity entity = repository.findById(partyRecruitId)
                                              .orElseThrow(() -> new NotExistException("존재하지 않는 파티 모집글 입니다.", partyRecruitId));

        entity.delete();

        return 1;
    }
}