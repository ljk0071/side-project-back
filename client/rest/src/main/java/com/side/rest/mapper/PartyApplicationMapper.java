package com.side.rest.mapper;

import com.side.domain.model.PartyApplication;
import com.side.rest.domain.party.dto.response.PartyApplicationResponseDto;
import com.side.rest.domain.party.dto.request.PartyApplicationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PartyApplicationMapper {

    PartyApplicationMapper PartyApplicationMapper = Mappers.getMapper(PartyApplicationMapper.class);

    @Mapping(target = "metadata", ignore = true)
    PartyApplication toDomain(PartyApplicationRequestDto dto);

    PartyApplicationResponseDto toResponse(PartyApplication partyApplication);

    List<PartyApplicationResponseDto> toResponseList(List<PartyApplication> partyApplications);
}