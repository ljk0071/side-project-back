package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import lombok.Builder;

@Builder(toBuilder = true)
public record PartyApplication(
        Long id,
        Long revision,
        Long partyRecruitId,
        Long resumeId,
        PartyApplicationStatusTypeEnum status,
        Metadata metadata
) {
}