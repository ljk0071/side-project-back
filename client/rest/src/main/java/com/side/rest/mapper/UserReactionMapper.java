package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.UserReaction;
import com.side.rest.domain.user.dto.request.UserReactionRecordRequestDto;
import com.side.rest.domain.user.dto.response.UserReactionRecordResponseDto;

@Mapper
public interface UserReactionMapper {

    UserReactionMapper UserReactionMapper = Mappers.getMapper(UserReactionMapper.class);

    UserReaction toDomain(UserReactionRecordRequestDto dto);

    UserReactionRecordResponseDto toResponse(UserReaction userReaction);
}