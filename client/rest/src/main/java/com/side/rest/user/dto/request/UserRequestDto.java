package com.side.rest.user.dto.request;

import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

	private Long uniqueId;

	@NotNull
	@Size(max = 50)
	private String id;

	@NotNull
	@Size(max = 255)
	private String password;

	@NotNull
	@Size(max = 50)
	private String name;

	@Size(max = 15)
	private String phoneNumber;

	@Size(max = 255)
	private String email;

	private UserStatus status;

	private UserType type;

	@Size(max = 65535)
	private String description;
}
