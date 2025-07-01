package com.side.rest.domain.resume.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyApplicationRequestDto {

    @Null(groups = Insert.class, message = "등록 시 ID는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "수정 시 ID는 필수입니다")
    @Positive(groups = Update.class, message = "ID는 양수여야 합니다")
    private Long id;
    @NotNull(groups = {Insert.class, Update.class}, message = "파티 모집글 ID는 필수입니다")
    @Positive(groups = {Insert.class, Update.class}, message = "파티 모집글 ID는 양수여야 합니다")
    private Long partyRecruitId;
    @NotNull(groups = {Insert.class, Update.class}, message = "이력서 ID는 필수입니다")
    @Positive(groups = {Insert.class, Update.class}, message = "이력서 ID는 양수여야 합니다")
    private Long resumeId;

    public interface Insert extends Default {}

    public interface Update extends Default {}
}