package com.side.rest.domain.board.controller;

import com.side.rest.ApiResponse;
import com.side.rest.domain.board.dto.request.PartyRecruitRequestDto;
import com.side.rest.domain.board.dto.request.SearchRequestDto;
import com.side.rest.domain.board.dto.response.PartyRecruitResponseDto;
import com.side.security.service.SecurityHelper;
import com.side.usecase.board.PartyRecruitUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.side.rest.mapper.PartyRecruitMapper.PartyRecruitMapper;
import static com.side.rest.mapper.SearchMapper.SearchMapper;

@Slf4j
@RestController
@RequestMapping("/v1/party")
@RequiredArgsConstructor
public class PartyRecruitController {

    private final PartyRecruitUseCase partyRecruitUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody @Validated(PartyRecruitRequestDto.Insert.class) PartyRecruitRequestDto partyRequestDto) {

        partyRequestDto.setUserUniqueId(SecurityHelper.getAuthenticatedUser().uniqueId());

        partyRecruitUseCase.create(PartyRecruitMapper.toDomain(partyRequestDto));

        return ResponseEntity.ok(ApiResponse.success("게시글 작성이 완료되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartyRecruitResponseDto>>> getActiveRecruits(
            @Validated SearchRequestDto dto
    ) {

        return ResponseEntity.ok(ApiResponse.success(null, partyRecruitUseCase.getActiveRecruits(SearchMapper.toDomain(dto))
                                                                              .stream()
                                                                              .map(PartyRecruitMapper::toResponse)
                                                                              .toList()));
    }

    @GetMapping("/{partyRecruitId}")
    public ResponseEntity<ApiResponse<PartyRecruitResponseDto>> findByRecruitId(
            @PathVariable(value = "partyRecruitId") Long partyRecruitId
    ) {

        return ResponseEntity.ok(ApiResponse.success(null, PartyRecruitMapper.toResponse(partyRecruitUseCase.findByRecruitId(partyRecruitId))));
    }
}
