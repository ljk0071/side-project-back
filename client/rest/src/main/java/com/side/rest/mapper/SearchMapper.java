package com.side.rest.mapper;

import com.side.domain.Search;
import com.side.rest.domain.board.dto.request.SearchRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SearchMapper {

    SearchMapper SearchMapper = Mappers.getMapper(SearchMapper.class);

    Search toDomain(SearchRequestDto dto);
}
