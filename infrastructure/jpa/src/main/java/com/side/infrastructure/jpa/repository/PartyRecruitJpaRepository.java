package com.side.infrastructure.jpa.repository;

import com.side.domain.model.PartyRecruit;
import com.side.domain.repository.PartyRecruitRepository;
import com.side.domain.repository.PartyRecruitRepositoryManager;
import com.side.infrastructure.jpa.entity.PartyRecruitEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.side.domain.RepositoryTypeEnum.JPA;
import static com.side.infrastructure.jpa.mapper.PartyRecruitMapper.PartyRecruitMapper;

@RequiredArgsConstructor
@Repository
public class PartyRecruitJpaRepository implements PartyRecruitRepository {

    private final PartyRecruitJpaInterface repository;

    @PostConstruct
    public void init() {
        PartyRecruitRepositoryManager.addPartyRecruitRepository(JPA, this);
    }

    @Override
    public void create(PartyRecruit partyRecruit) {

        PartyRecruitEntity entity = PartyRecruitMapper.toEntity(partyRecruit);

        repository.save(entity);
    }

    @Override
    public void delete(long partyId) {
        throw new UnsupportedOperationException("jpa에서는 비효율적이므로 soft delete를 하지 않음");
    }
}