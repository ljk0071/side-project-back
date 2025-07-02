package com.side.rest.mapper;

import com.side.domain.model.LoginAttemptLog;
import com.side.rest.domain.user.dto.request.LoginAttemptLogRequestDto;
import com.side.rest.domain.user.dto.response.LoginAttemptLogResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoginAttemptLogMapper {

    LoginAttemptLogMapper LoginAttemptLogMapper = Mappers.getMapper(LoginAttemptLogMapper.class);

    LoginAttemptLog toDomain(LoginAttemptLogRequestDto dto);

    LoginAttemptLogResponseDto toResponse(LoginAttemptLog loginAttemptLog);
}