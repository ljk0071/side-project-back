package com.side.infrastructure.jpa.repository;

import static com.side.domain.enums.RepositoryTypeEnum.*;
import static com.side.infrastructure.jpa.mapper.RoleHierarchyMapper.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.side.domain.model.RoleHierarchyInfo;
import com.side.domain.repository.RoleHierarchyRepository;
import com.side.domain.repository.RoleHierarchyRepositoryManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoleHierarchyRepositoryImpl implements RoleHierarchyRepository {

	private final RoleHierarchyJpaRepository repository;

	@PostConstruct
	public void init() {
		RoleHierarchyRepositoryManager.addRoleHierarchyRepository(JPA, this);
	}

	@Override
	public List<RoleHierarchyInfo> findAll() {

		return repository.findAll().stream()
						 .map(RoleHierarchyMapper::toDomain)
						 .toList();
	}
}
