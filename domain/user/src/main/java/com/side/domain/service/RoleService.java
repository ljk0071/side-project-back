package com.side.domain.service;

import org.springframework.stereotype.Service;

import com.side.domain.model.Role;
import com.side.domain.repository.RoleRepositoryManager;

@Service
public class RoleService {

	public Role findById(String roleId) {
		return RoleRepositoryManager.getDefaultRoleRepository().findById(roleId);
	}
}