package com.side.domain.service;

import com.side.domain.model.Role;
import com.side.domain.repository.RoleReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleReader roleReader;

    public Role findById(String roleId) {
        return roleReader.findById(roleId);
    }
}