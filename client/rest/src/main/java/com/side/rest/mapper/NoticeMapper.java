package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.Notice;
import com.side.rest.board.dto.request.NoticeRequestDto;
import com.side.rest.board.dto.response.NoticeResponseDto;

@Mapper
public interface NoticeMapper {

	NoticeMapper NoticeMapper = Mappers.getMapper(NoticeMapper.class);

	@Mapping(target = "comment", ignore = true)
	@Mapping(target = "article.userReaction", ignore = true)
	@Mapping(target = "metadata", ignore = true)
	Notice toDomain(NoticeRequestDto dto);

	NoticeResponseDto toResponse(Notice notice);
}