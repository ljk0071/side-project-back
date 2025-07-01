package com.side.rest.mapper;

import com.side.domain.model.Notice;
import com.side.rest.domain.board.dto.request.NoticeRequestDto;
import com.side.rest.domain.board.dto.response.NoticeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoticeMapper {

    NoticeMapper NoticeMapper = Mappers.getMapper(NoticeMapper.class);

    @Mapping(target = "metadata", ignore = true)
    Notice toDomain(NoticeRequestDto dto);

    NoticeResponseDto toResponse(Notice notice);
}