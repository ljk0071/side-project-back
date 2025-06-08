package com.side.domain.repository;

import com.side.domain.model.Role;

public interface RoleRepository {

	Role findById(String roleId);
}

