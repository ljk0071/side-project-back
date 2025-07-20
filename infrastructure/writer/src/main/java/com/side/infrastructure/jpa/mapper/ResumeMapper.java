package com.side.infrastructure.jpa.mapper;

import com.side.domain.model.Resume;
import com.side.infrastructure.jpa.entity.ResumeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResumeMapper {

    ResumeMapper ResumeMapper = Mappers.getMapper(ResumeMapper.class);

    Resume toDomain(ResumeEntity entity);

    @Mapping(target = "id", ignore = true)
    ResumeEntity toEntity(Resume resume);
}