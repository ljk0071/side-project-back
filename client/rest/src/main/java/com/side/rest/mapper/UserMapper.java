package com.side.rest.mapper;

import com.side.domain.model.User;
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

    UserResponseDto toResponse(User user);
}
