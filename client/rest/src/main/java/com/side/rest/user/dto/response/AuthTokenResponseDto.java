package com.side.rest.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthTokenResponseDto {

	private String accessToken;
	private String accessRefreshToken;
	private long expiresIn;
	private long refreshExpiresIn;

}
