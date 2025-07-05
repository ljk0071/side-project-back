package com.side.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoDeleteStatus {

    YES("Y", "활성"),
    NO("N", "비활성"),
    DELETE("D", "삭제"),
    ;

    private final String value;
    private final String note;

    public static YesNoDeleteStatus fromCode(String code) {

        return switch (code) {
            case "Y" -> YES;
            case "N" -> NO;
            case "D" -> DELETE;
            default -> throw new IllegalArgumentException();
        };
    }
}
