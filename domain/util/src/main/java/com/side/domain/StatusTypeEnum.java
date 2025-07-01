package com.side.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusTypeEnum {

    Y("활성"),
    N("비활성"),
    D("삭제");


    private final String value;
}
