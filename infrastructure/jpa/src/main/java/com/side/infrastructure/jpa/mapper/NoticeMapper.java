package com.side.infrastructure.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.Notice;
import com.side.infrastructure.jpa.entity.NoticeEntity;

@Mapper
public interface NoticeMapper {

	NoticeMapper NoticeMapper = Mappers.getMapper(NoticeMapper.class);

	Notice toDomain(NoticeEntity entity);

	@Mapping(target = "id", ignore = true)
	NoticeEntity toEntity(Notice notice);
}
