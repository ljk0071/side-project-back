package com.side.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {

    PENDING("P", "승인대기"),
    ACTIVE("A", "정상"),
    LOCKED("L", "잠금"),
    DELETED("D", "삭제"),
    ;

    private final String value;
    private final String note;
}
