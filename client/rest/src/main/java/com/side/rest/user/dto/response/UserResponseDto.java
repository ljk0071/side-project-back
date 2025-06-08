package com.side.rest.user.dto.response;

import java.util.List;

import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;
import com.side.domain.model.Role;
import com.side.rest.board.dto.response.MetadataResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {

	private long uniqueId;
	private String id;
	private String password;
	private String name;
	private UserStatus status;
	private UserType type;
	private String email;
	private String phoneNumber;
	private String description;
	private List<Role> roles;
	private MetadataResponseDto metadata;
}
