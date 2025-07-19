package com.side.infrastructure.jpa.mapper;

import com.side.domain.model.PartyApplication;
import com.side.infrastructure.jpa.entity.PartyApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartyApplicationMapper {

    PartyApplicationMapper PartyApplicationMapper = Mappers.getMapper(PartyApplicationMapper.class);

    PartyApplication toDomain(PartyApplicationEntity entity);

    PartyApplicationEntity toEntity(PartyApplication partyApplication);
}