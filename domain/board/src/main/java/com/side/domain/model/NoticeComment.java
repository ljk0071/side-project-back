package com.side.domain.model;

import com.side.domain.Metadata;
import com.side.domain.YesNoDeleteStatus;
import lombok.Builder;

@Builder(toBuilder = true)
public record NoticeComment(
        Long id,
        Long revision,
        Long parentCommentId,
        String contents,
        Long noticeId,
        YesNoDeleteStatus status,
        Metadata metadata
) {
}