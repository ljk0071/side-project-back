package com.side.domain.model;

import lombok.Builder;

/**
 * 인증 토큰을 나타내는 도메인 모델.
 * 액세스 토큰과 리프레시 토큰 및 각각의 만료 시간에 대한 정보를 포함합니다.
 */
@Builder(toBuilder = true)
public record AuthToken(
	String userId,
	String password,
	int days,
	String accessToken,
	String refreshToken,
	long expiresIn,
	long refreshExpiresIn
) {
	/**
	 * AuthToken을 생성합니다.
	 *
	 * @param userId 토큰과 연관된 사용자 ID
	 * @param accessToken 액세스 토큰 문자열
	 * @param refreshToken 리프레시 토큰 문자열
	 * @param expiresIn 액세스 토큰의 만료 시간(초 단위)
	 * @param refreshExpiresIn 리프레시 토큰의 만료 시간(초 단위)
	 */
	public AuthToken {
		// Compact constructor for validation if needed
	}
}
