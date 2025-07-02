package com.side.rest.domain.board.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRequestDto {

    @NotEmpty(groups = {Insert.class, Update.class}, message = "검색 조건은 최소 1개 이상 선택해야 합니다")
    private List<String> searchConditions;
    @Size(min = 1, max = 100, groups = {Insert.class, Update.class}, message = "검색 키워드는 1-100자 사이여야 합니다")
    private String searchKeyword;

    public interface Insert extends Default {}

    public interface Update extends Default {}
}