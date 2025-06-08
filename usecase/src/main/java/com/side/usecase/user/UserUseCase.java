package com.side.usecase.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.side.domain.model.User;
import com.side.domain.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserUseCase {

	private final UserService userService;

	@Transactional
	public void create(User user) {
		userService.create(user);
	}

}