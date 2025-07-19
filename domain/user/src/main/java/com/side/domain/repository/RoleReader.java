package com.side.domain.repository;

import com.side.domain.model.Role;

public interface RoleReader {

    Role findById(String roleId);
}

