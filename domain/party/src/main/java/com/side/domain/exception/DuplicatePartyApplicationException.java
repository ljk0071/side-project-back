package com.side.domain.exception;

import lombok.Getter;

@Getter
public class DuplicatePartyApplicationException extends RuntimeException {

    private final long partyRecruitId;
    private final String title;

    public DuplicatePartyApplicationException(String message, long partyRecruitId, String title) {
        super(message);
        this.partyRecruitId = partyRecruitId;
        this.title = title;
    }
}
