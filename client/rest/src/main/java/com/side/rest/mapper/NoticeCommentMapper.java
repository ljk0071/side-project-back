package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.NoticeComment;
import com.side.rest.domain.board.dto.request.NoticeCommentRequestDto;
import com.side.rest.domain.board.dto.response.NoticeCommentResponseDto;

@Mapper
public interface NoticeCommentMapper {

    NoticeCommentMapper INSTANCE = Mappers.getMapper(NoticeCommentMapper.class);

    @Mapping(target = "metadata", ignore = true)
    NoticeComment toDomain(NoticeCommentRequestDto dto);

    NoticeCommentResponseDto toResponse(NoticeComment noticeComment);
}