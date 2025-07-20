package com.side.domain.service;


import com.side.domain.model.Role;
import com.side.domain.repository.UserRoleReader;
import com.side.domain.repository.UserRoleWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserRoleService {

    private final UserRoleReader userRoleReader;
    private final UserRoleWriter userRoleWriter;

    public Collection<Role> loadRoleByUserId(String userId) {
        return userRoleReader.loadRoleByUserId(userId);
    }

    public long createNormalUser(long userUniqueId) {
        return userRoleWriter.createNormalUser(userUniqueId);
    }
}
