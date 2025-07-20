package com.side.domain.repository;

import com.side.domain.model.RoleHierarchyInfo;

import java.util.List;

public interface RoleHierarchyReader {

    List<RoleHierarchyInfo> findAll();
}

