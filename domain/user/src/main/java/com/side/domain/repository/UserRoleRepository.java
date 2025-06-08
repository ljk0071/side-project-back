package com.side.domain.repository;

import java.util.Collection;

import com.side.domain.model.Role;

public interface UserRoleRepository {

	Collection<Role> loadRoleByUserId(String userId);
}
