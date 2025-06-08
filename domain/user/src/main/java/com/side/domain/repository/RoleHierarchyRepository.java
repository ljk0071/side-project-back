package com.side.domain.repository;

import java.util.List;

import com.side.domain.model.RoleHierarchyInfo;

public interface RoleHierarchyRepository {

	List<RoleHierarchyInfo> findAll();
}

