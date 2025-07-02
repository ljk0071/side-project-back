package com.side.rest.mapper;

import com.side.domain.model.UserReaction;
import com.side.rest.domain.user.dto.request.UserReactionRecordRequestDto;
import com.side.rest.domain.user.dto.response.UserReactionRecordResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserReactionMapper {

    UserReactionMapper UserReactionMapper = Mappers.getMapper(UserReactionMapper.class);

    UserReaction toDomain(UserReactionRecordRequestDto dto);

    UserReactionRecordResponseDto toResponse(UserReaction userReaction);
}