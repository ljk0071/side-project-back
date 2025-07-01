package com.side.infrastructure.jpa.repository;

import com.side.domain.model.RoleHierarchyInfo;
import com.side.domain.repository.RoleHierarchyRepository;
import com.side.domain.repository.RoleHierarchyRepositoryManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.side.domain.RepositoryTypeEnum.JPA;
import static com.side.infrastructure.jpa.mapper.RoleHierarchyMapper.RoleHierarchyMapper;

@Repository
@RequiredArgsConstructor
public class RoleHierarchyJpaRepository implements RoleHierarchyRepository {

    private final RoleHierarchyJpaInterface repository;

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
