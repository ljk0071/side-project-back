package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.PartyRecruit;
import com.side.rest.board.dto.request.PartyRecruitRequestDto;
import com.side.rest.board.dto.response.PartyRecruitResponseDto;

@Mapper
public interface PartyRecruitMapper {

	PartyRecruitMapper PartyRecruitMapper = Mappers.getMapper(PartyRecruitMapper.class);

	@Mapping(target = "comment", ignore = true)
	@Mapping(target = "article.userReaction", ignore = true)
	@Mapping(target = "metadata", ignore = true)
	PartyRecruit toDomain(PartyRecruitRequestDto dto);

	PartyRecruitResponseDto toResponse(PartyRecruit partyRecruit);
}