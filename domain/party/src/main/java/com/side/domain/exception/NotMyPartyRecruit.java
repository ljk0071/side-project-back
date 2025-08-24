package com.side.domain.exception;

import lombok.Getter;

@Getter
public class NotMyPartyRecruit extends RuntimeException {

    private long partyRecruitId;
    private long userUniqueId;

    public NotMyPartyRecruit(String message, long partyRecruitId, long userUniqueId) {
        super(message);
        this.partyRecruitId = partyRecruitId;
        this.userUniqueId = userUniqueId;
    }
}
