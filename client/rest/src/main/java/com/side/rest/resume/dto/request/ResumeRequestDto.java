package com.side.rest.resume.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeRequestDto {

	private Long id;

	private Long userUniqueId;

	private String contents;
}