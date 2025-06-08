package com.side.infrastructure.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.Role;
import com.side.domain.model.RoleHierarchyInfo;
import com.side.infrastructure.jpa.entity.RoleHierarchyEntity;

@Mapper
public interface RoleHierarchyMapper {

	RoleHierarchyMapper RoleHierarchyMapper = Mappers.getMapper(RoleHierarchyMapper.class);

	@Mapping(target = "lowerRole", source = "lowerRole", qualifiedByName = "stringToRole")
	@Mapping(target = "higherRole", source = "higherRole", qualifiedByName = "stringToRole")
	RoleHierarchyInfo toDomain(RoleHierarchyEntity entity);

	@Mapping(target = "lowerRole", source = "lowerRole.id")
	@Mapping(target = "higherRole", source = "higherRole.id")
	RoleHierarchyEntity toEntity(RoleHierarchyInfo roleHierarchyInfo);

	@Named("stringToRole")
	default Role stringToRole(String roleId) {
		if (roleId == null) {
			return null;
		}

		return Role.builder()
				   .id(roleId)
				   .build();
	}
}