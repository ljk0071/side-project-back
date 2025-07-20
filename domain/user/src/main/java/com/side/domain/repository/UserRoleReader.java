package com.side.domain.repository;

import com.side.domain.model.Role;

import java.util.Collection;

public interface UserRoleReader {

    Collection<Role> loadRoleByUserId(String userId);
}
