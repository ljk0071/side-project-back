package com.side.usecase.user;

import com.side.domain.model.User;
import com.side.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;

    @Transactional
    public void create(User user) {
        userService.create(user);
    }

}