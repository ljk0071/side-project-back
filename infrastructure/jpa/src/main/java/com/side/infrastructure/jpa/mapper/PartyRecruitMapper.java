package com.side.infrastructure.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.PartyRecruit;
import com.side.infrastructure.jpa.entity.PartyRecruitEntity;

@Mapper
public interface PartyRecruitMapper {

	PartyRecruitMapper PartyRecruitMapper = Mappers.getMapper(PartyRecruitMapper.class);

	PartyRecruit toDomain(PartyRecruitEntity entity);

	@Mapping(target = "id", ignore = true)
	PartyRecruitEntity toEntity(PartyRecruit partyRecruit);
}
