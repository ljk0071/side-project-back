package com.side.domain.service;

import com.side.domain.model.Role;
import com.side.domain.repository.RoleRepositoryManager;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    public Role findById(String roleId) {
        return RoleRepositoryManager.getDefaultRoleRepository().findById(roleId);
    }
}