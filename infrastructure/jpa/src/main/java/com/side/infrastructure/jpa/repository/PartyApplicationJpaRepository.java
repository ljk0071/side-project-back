package com.side.infrastructure.jpa.repository;

import com.side.domain.model.PartyApplication;
import com.side.domain.repository.PartyApplicationWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.side.infrastructure.jpa.mapper.PartyApplicationMapper.PartyApplicationMapper;

@RequiredArgsConstructor
@Repository
public class PartyApplicationJpaRepository implements PartyApplicationWriter {

    private final PartyApplicationJpaInterface repository;

    @Override
    public long create(PartyApplication partyApplication) {

        return repository.save(PartyApplicationMapper.toEntity(partyApplication)).getId();
    }
}