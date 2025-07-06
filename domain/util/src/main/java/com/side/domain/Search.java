package com.side.domain;

import com.side.domain.enums.SearchType;
import lombok.Builder;

import java.util.List;

@Builder
public record Search(List<SearchType> searchConditions, String searchKeyword) {
}
