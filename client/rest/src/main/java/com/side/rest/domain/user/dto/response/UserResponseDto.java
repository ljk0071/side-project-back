package com.side.rest.domain.user.dto.response;

import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;
import com.side.rest.domain.board.dto.response.MetadataResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String userId;
    private String name;
    private UserStatus status;
    private UserType type;
    private String email;
    private String description;
    private MetadataResponseDto metadata;
}
