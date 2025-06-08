package com.side.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.side.infrastructure.jpa.entity.RoleHierarchyEntity;
import com.side.infrastructure.jpa.entity.RoleHierarchyKey;

public interface RoleHierarchyJpaRepository extends JpaRepository<RoleHierarchyEntity, RoleHierarchyKey> {
}