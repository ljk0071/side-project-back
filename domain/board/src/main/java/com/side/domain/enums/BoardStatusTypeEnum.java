package com.side.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardStatusTypeEnum {

	Y("사용가능"),
	N("사용불가능"),
	D("삭제됨");


	private String value;
}
