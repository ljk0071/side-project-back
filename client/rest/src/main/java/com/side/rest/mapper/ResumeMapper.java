package com.side.rest.mapper;

import com.side.domain.model.Resume;
import com.side.rest.domain.party.dto.request.ResumeRequestDto;
import com.side.rest.domain.party.dto.response.ResumeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResumeMapper {

    ResumeMapper ResumeMapper = Mappers.getMapper(ResumeMapper.class);

    @Mapping(target = "metadata", ignore = true)
    Resume toDomain(ResumeRequestDto dto);

    ResumeResponseDto toResponse(Resume resume);
}