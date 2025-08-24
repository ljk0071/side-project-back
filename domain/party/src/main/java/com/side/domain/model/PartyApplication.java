package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import lombok.Builder;

@Builder(toBuilder = true)
public record PartyApplication(
        Long id,
        Long revision,
        PartyRecruit partyRecruit,
        Resume resume,
        PartyApplicationStatusTypeEnum status,
        Metadata metadata
) {
}