package com.side.rest.mapper;

import com.side.domain.model.PartyApplication;
import com.side.rest.domain.board.dto.response.PartyApplicationResponseDto;
import com.side.rest.domain.party.dto.request.PartyApplicationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartyApplicationMapper {

    PartyApplicationMapper PartyApplicationMapper = Mappers.getMapper(PartyApplicationMapper.class);

    @Mapping(target = "metadata", ignore = true)
    PartyApplication toDomain(PartyApplicationRequestDto dto);

    PartyApplicationResponseDto toResponse(PartyApplication partyApplication);
}