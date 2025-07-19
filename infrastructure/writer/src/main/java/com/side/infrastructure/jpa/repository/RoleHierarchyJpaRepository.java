package com.side.infrastructure.jpa.repository;

import com.side.domain.model.RoleHierarchyInfo;
import com.side.domain.repository.RoleHierarchyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.side.infrastructure.jpa.mapper.RoleHierarchyMapper.RoleHierarchyMapper;

@Repository
@RequiredArgsConstructor
public class RoleHierarchyJpaRepository implements RoleHierarchyReader {

    private final RoleHierarchyJpaInterface repository;

    @Override
    public List<RoleHierarchyInfo> findAll() {

        return repository.findAll().stream()
                         .map(RoleHierarchyMapper::toDomain)
                         .toList();
    }
}
