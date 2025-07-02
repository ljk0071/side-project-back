package com.side.domain.repository;

import com.side.domain.model.Role;

import java.util.Collection;

public interface UserRoleRepository {

    Collection<Role> loadRoleByUserId(String userId);
}
