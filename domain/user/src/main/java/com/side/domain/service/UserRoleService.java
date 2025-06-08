package com.side.domain.service;

import static com.side.domain.enums.RepositoryTypeEnum.*;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.side.domain.model.Role;
import com.side.domain.repository.UserRoleRepositoryManager;

@Service
public class UserRoleService {

	public Collection<Role> loadRoleByUserId(String userId) {
		return UserRoleRepositoryManager.getUserRoleRepository(JOOQ).loadRoleByUserId(userId);
	}
}
