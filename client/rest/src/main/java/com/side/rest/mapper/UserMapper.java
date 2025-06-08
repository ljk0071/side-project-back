package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.User;
import com.side.rest.user.dto.request.UserRequestDto;
import com.side.rest.user.dto.response.UserResponseDto;

@Mapper
public interface UserMapper {

	UserMapper UserMapper = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "metadata", ignore = true)
	User toDomain(UserRequestDto userRequestDto);

	UserResponseDto toResponse(User user);
}
