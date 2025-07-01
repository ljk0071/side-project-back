package com.side.infrastructure.jpa.mapper;

import com.side.domain.model.User;
import com.side.infrastructure.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper UserMapper = Mappers.getMapper(UserMapper.class);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User user);
}
