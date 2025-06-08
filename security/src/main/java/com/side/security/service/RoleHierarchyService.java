package com.side.security.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {

	private final RoleHierarchy roleHierarchy;

	public List<String> getSecurityRoleHierarchy() {
		return SecurityContextHolder.getContext()
									.getAuthentication()
									.getAuthorities()
									.stream()
									.map(GrantedAuthority::getAuthority)
									.toList();
	}

	public Collection<GrantedAuthority> getGrantedAuthorities(Collection<String> roles) {
		return roles.stream()
					.flatMap(role -> roleHierarchy.getReachableGrantedAuthorities(
													  Collections.singleton(new SimpleGrantedAuthority(role)))
												  .stream())
					.map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
					.collect(Collectors.toSet());

	}
}