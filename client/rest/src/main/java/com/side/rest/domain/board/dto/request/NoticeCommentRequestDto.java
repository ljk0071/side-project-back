package com.side.rest.domain.board.dto.request;

import com.side.domain.YesNoDeleteStatus;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCommentRequestDto {

    @Null(groups = Insert.class, message = "등록 시 ID는 null이어야 합니다")
    @NotNull(groups = Update.class, message = "수정 시 ID는 필수입니다")
    @Positive(groups = Update.class, message = "ID는 양수여야 합니다")
    private Long id;
    @NotNull(groups = Update.class, message = "수정 시 버전은 필수입니다")
    private Long revision;
    @NotBlank(groups = {Insert.class, Update.class}, message = "댓글 내용은 필수입니다")
    @Size(max = 255, message = "댓글 내용은 255자를 초과할 수 없습니다")
    private String contents;
    @NotNull(groups = {Insert.class, Update.class}, message = "상태는 필수입니다")
    private YesNoDeleteStatus status;
    @NotNull(groups = {Insert.class, Update.class}, message = "공지사항 ID는 필수입니다")
    @Positive(message = "공지사항 ID는 양수여야 합니다")
    private Long noticeId;
    @Positive(message = "부모 댓글 ID는 양수여야 합니다")
    private Long parentCommentId;

    public interface Insert extends Default {}

    public interface Update extends Default {}
}