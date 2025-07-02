package com.side.domain.repository;

import com.side.domain.model.RoleHierarchyInfo;

import java.util.List;

public interface RoleHierarchyRepository {

    List<RoleHierarchyInfo> findAll();
}

