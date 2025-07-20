package com.side.infrastructure.jpa.repository;

import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitWriter;
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
}