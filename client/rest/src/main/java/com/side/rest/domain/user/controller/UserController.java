package com.side.rest.domain.user.controller;

import com.side.rest.domain.user.dto.request.UserRequestDto;
import com.side.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.side.rest.mapper.UserMapper.UserMapper;

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
}
