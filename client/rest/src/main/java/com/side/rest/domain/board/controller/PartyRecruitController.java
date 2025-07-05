package com.side.rest.domain.board.controller;

import com.side.rest.domain.board.dto.request.PartyRecruitRequestDto;
import com.side.security.service.SecurityHelper;
import com.side.usecase.board.PartyRecruitUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.side.rest.mapper.PartyRecruitMapper.PartyRecruitMapper;

@Slf4j
@RestController
@RequestMapping("/v1/party")
@RequiredArgsConstructor
public class PartyRecruitController {

    private final PartyRecruitUseCase partyRecruitUseCase;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated(PartyRecruitRequestDto.Insert.class) PartyRecruitRequestDto partyRequestDto) {

        partyRequestDto.setUserUniqueId(SecurityHelper.getAuthenticatedUser().uniqueId());

        partyRecruitUseCase.create(PartyRecruitMapper.toDomain(partyRequestDto));

        return ResponseEntity.ok(null);
    }
}
