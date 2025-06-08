package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.AuthToken;
import com.side.rest.user.dto.request.AuthTokenRequestDto;
import com.side.rest.user.dto.response.AuthTokenResponseDto;

/**
 * Mapper for converting between AuthToken domain model and DTOs.
 */
@Mapper
public interface AuthTokenMapper {

	AuthTokenMapper AuthTokenMapper = Mappers.getMapper(AuthTokenMapper.class);

	/**
	 * Converts AuthTokenRequestDto to AuthToken domain model.
	 * Note: This only maps the userId field as other fields are generated during authentication.
	 *
	 * @param requestDto the request DTO
	 * @return the domain model
	 */
	@Mapping(target = "accessToken", ignore = true)
	@Mapping(target = "refreshToken", ignore = true)
	@Mapping(target = "expiresIn", ignore = true)
	@Mapping(target = "refreshExpiresIn", ignore = true)
	AuthToken toDomain(AuthTokenRequestDto requestDto);

	/**
	 * Converts AuthToken domain model to AuthTokenResponseDto.
	 * Maps refreshToken to accessRefreshToken to match the DTO field name.
	 *
	 * @param authToken the domain model
	 * @return the response DTO
	 */
	@Mapping(source = "refreshToken", target = "accessRefreshToken")
	AuthTokenResponseDto toResponse(AuthToken authToken);
}