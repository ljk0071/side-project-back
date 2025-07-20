package com.side.rest.mapper;

import com.side.domain.model.DiscordInfoResponseDto;
import com.side.domain.model.User;
import com.side.domain.model.UserDiscordAuth;
import com.side.rest.domain.user.dto.request.UserRequestDto;
import com.side.rest.domain.user.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper UserMapper = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "metadata", ignore = true)
    User toDomain(UserRequestDto userRequestDto);

    User toDomain(UserDiscordAuth userDiscordAuth);

    @Mapping(target = "metadata", ignore = true)
    UserDiscordAuth toDomain(DiscordInfoResponseDto.UserInfo discordUserInfo);

    UserResponseDto toResponse(User user);
}
