package com.side.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.side.domain.model.Notification;
import com.side.rest.domain.user.dto.request.NotificationRequestDto;
import com.side.rest.domain.user.dto.response.NotificationResponseDto;

@Mapper
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "metadata", ignore = true)
    Notification toDomain(NotificationRequestDto dto);

    NotificationResponseDto toResponse(Notification notification);
}