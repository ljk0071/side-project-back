package com.side.rest.user.controller;

import static com.side.rest.mapper.UserMapper.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.domain.ExternalApiException;
import com.side.rest.user.dto.request.UserRequestDto;
import com.side.rest.user.dto.response.UserResponseDto;
import com.side.usecase.user.UserUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserUseCase userUseCase;

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody UserRequestDto dto) {

		userUseCase.create(UserMapper.toDomain(dto));

		return ResponseEntity.ok(null);
	}

	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {

		if (true)
			throw new ExternalApiException("asds");

		return null;
	}
}
