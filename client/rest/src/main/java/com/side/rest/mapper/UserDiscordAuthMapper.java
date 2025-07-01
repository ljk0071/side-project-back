package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.UserDiscordAuth;
import com.side.rest.domain.user.dto.request.UserDiscordAuthRequestDto;
import com.side.rest.domain.user.dto.response.UserDiscordAuthResponseDto;

@Mapper
public interface UserDiscordAuthMapper {

    UserDiscordAuthMapper INSTANCE = Mappers.getMapper(UserDiscordAuthMapper.class);

    @Mapping(target = "metadata", ignore = true)
    UserDiscordAuth toDomain(UserDiscordAuthRequestDto dto);

    UserDiscordAuthResponseDto toResponse(UserDiscordAuth userDiscordAuth);
}