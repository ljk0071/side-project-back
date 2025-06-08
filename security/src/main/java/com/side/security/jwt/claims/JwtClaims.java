package com.side.security.jwt.claims;

import java.util.Collection;

import com.side.security.jwt.enums.JwtTokenType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JwtClaims {

	private String userId;
	private JwtTokenType tokenType;
	private Collection<String> roles;
}

