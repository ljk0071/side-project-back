package com.side.rest.domain.party.controller;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.rest.domain.board.dto.response.PartyApplicationResponseDto;
import com.side.rest.domain.party.dto.request.PartyApplicationRequestDto;
import com.side.usecase.resume.PartyApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/party/application")
@RequiredArgsConstructor
public class PartyApplicationController {

    private final PartyApplicationUseCase partyApplicationUseCase;

    @PostMapping
    public ResponseEntity<PartyApplicationResponseDto> create(
            @RequestBody @Validated(PartyApplicationRequestDto.Insert.class) PartyApplicationRequestDto dto
    ) {

        partyApplicationUseCase.create(dto.getPartyRecruitId(), dto.getResumeId());

        return ResponseEntity.ok(null);
    }

    @PatchMapping("/{partyApplicationId}/{statusType}")
    public ResponseEntity<String> changeStatus(
            @PathVariable("partyApplicationId") long partyApplicationId,
            @PathVariable("statusType") PartyApplicationStatusTypeEnum statusType
    ) {

        return ResponseEntity.ok(partyApplicationUseCase.changeStatus(partyApplicationId, statusType));
    }
}