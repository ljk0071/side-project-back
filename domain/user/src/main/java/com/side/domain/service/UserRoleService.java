package com.side.domain.service;


import com.side.domain.model.Role;
import com.side.domain.repository.UserRoleRepositoryManager;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.side.domain.RepositoryTypeEnum.JOOQ;

@Service
public class UserRoleService {

    public Collection<Role> loadRoleByUserId(String userId) {
        return UserRoleRepositoryManager.getUserRoleRepository(JOOQ).loadRoleByUserId(userId);
    }
}
