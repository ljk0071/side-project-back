package com.side.rest.domain.board.dto.request;

import com.side.domain.YesNoDeleteStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyRecruitRequestDto {

    @Null(groups = Insert.class, message = "Insert 시 id는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "Update 시 id는 필수입니다")
    @Positive(groups = Update.class)
    private Long id;
    @Null(groups = Insert.class, message = "Insert 시 id는 null이어야 합니다")
    private Long userUniqueId;
    @Valid
    @NotNull(message = "게시글 정보는 필수입니다")
    private ArticleRequestDto article;
    @NotNull(message = "최대 인원은 필수입니다")
    @Min(value = 2, message = "최대 인원은 2명 이상이어야 합니다")
    @Max(value = 6, message = "최대 인원은 6명 이하여야 합니다")
    private Integer maxMembers;
    @Null(groups = Insert.class, message = "등록 시 status는 null이어야 합니다")
    private YesNoDeleteStatus status;

    public interface Insert extends Default {}

    public interface Update extends Default {}
}