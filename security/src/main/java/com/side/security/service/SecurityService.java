package com.side.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.side.domain.model.User;
import com.side.domain.service.UserService;
import com.side.security.jwt.dto.SecurityDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

	private final UserService userService;

	@Override
	public SecurityDto loadUserByUsername(String userId) throws UsernameNotFoundException {

		User user = userService.findByUserId(userId);

		return SecurityDto.builder()
						  .uniqueId(user.uniqueId())
						  .userId(userId)
						  .password(user.password())
						  .authorities(user.roles().stream()
										   .map(role -> new SimpleGrantedAuthority(role.id()))
										   .toList())
						  .build();
	}

}
