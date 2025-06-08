package com.side.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {

	PENDING("승인대기", "", "", ""),
	ACTIVE("정상", "", "", ""),
	LOCKED("잠금", "", "", ""),
	DELETED("삭제", "", "", ""),
	;

	private final String name;
	private final String desc;
	private final String note;
	private final String link;
}
