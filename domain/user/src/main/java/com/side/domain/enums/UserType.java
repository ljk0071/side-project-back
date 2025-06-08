package com.side.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {

	NORMAL("일반", "", "", ""),
	ADMIN("관리자", "", "", "");

	private final String name;
	private final String desc;
	private final String note;
	private final String link;

}