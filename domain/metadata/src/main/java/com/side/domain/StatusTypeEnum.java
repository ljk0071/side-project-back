package com.side.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusTypeEnum {

	Y("사용가능"),
	N("사용불가능"),
	D("삭제됨");

	private final String value;
}
