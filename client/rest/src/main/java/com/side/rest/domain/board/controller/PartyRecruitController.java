package com.side.rest.domain.board.controller;

import static com.side.rest.mapper.PartyRecruitMapper.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.rest.domain.board.dto.request.PartyRecruitRequestDto;
import com.side.usecase.board.PartyRecruitUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/party")
@RequiredArgsConstructor
public class PartyRecruitController {

    private final PartyRecruitUseCase partyRecruitUseCase;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated(PartyRecruitRequestDto.Insert.class) PartyRecruitRequestDto partyRequestDto) {

        partyRequestDto.setUserUniqueId(1L);

        partyRecruitUseCase.create(PartyRecruitMapper.toDomain(partyRequestDto));

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{partyRecruitId}")
    public ResponseEntity<Void> delete(@PathVariable long partyRecruitId) {

        partyRecruitUseCase.delete(partyRecruitId);

        return ResponseEntity.ok(null);
    }
}
