package com.side.domain.model;

import com.side.domain.Metadata;
import lombok.Builder;

@Builder(toBuilder = true)
public record PartyApplication(
        Long id,
        Long revision,
        Long partyRecruitId,
        Long resumeId,
        String status,
        Metadata metadata
) {
}