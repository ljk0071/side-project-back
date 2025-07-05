package com.side.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartyApplicationStatusTypeEnum {

    // Party Application Status
    PENDING("P", "대기중"),
    ACCEPTED("A", "승인됨"),
    REJECTED("R", "거부됨"),
    CANCELED("C", "취소됨"),
    ;

    private final String value;
    private final String note;

    public static PartyApplicationStatusTypeEnum fromCode(String code) {
        return switch (code) {
            case "P" -> PartyApplicationStatusTypeEnum.PENDING;
            case "A" -> PartyApplicationStatusTypeEnum.ACCEPTED;
            case "R" -> PartyApplicationStatusTypeEnum.REJECTED;
            case "C" -> PartyApplicationStatusTypeEnum.CANCELED;
            default -> throw new IllegalArgumentException("Unknown code: " + code);
        };
    }
}
