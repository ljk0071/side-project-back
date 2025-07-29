package com.side.rest.domain.party.controller;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.domain.model.PartyApplication;
import com.side.rest.ApiResponse;
import com.side.rest.domain.party.dto.request.PartyApplicationRequestDto;
import com.side.rest.domain.party.dto.response.PartyApplicationResponseDto;
import com.side.security.service.SecurityHelper;
import com.side.usecase.resume.PartyApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.side.rest.mapper.PartyApplicationMapper.PartyApplicationMapper;

@Slf4j
@RestController
@RequestMapping("/v1/party/application")
@RequiredArgsConstructor
public class PartyApplicationController {

    private final PartyApplicationUseCase partyApplicationUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
            @RequestBody @Validated(PartyApplicationRequestDto.Insert.class) PartyApplicationRequestDto dto
    ) {

        partyApplicationUseCase.create(dto.getPartyRecruitId(), dto.getResumeId());

        return ResponseEntity.ok(ApiResponse.success("지원에 성공하였습니다.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartyApplicationResponseDto>>> findByCurrentUser() {
        Long userUniqueId = SecurityHelper.getAuthenticatedUserUniqueId();

        List<PartyApplication> applications = partyApplicationUseCase.findByUserUniqueId(userUniqueId);
        List<PartyApplicationResponseDto> responseList = PartyApplicationMapper.toResponseList(applications);

        return ResponseEntity.ok(ApiResponse.success("내가 지원한 파티 목록 조회 성공", responseList));
    }


    @PatchMapping("/{partyApplicationId}/{statusType}")
    public ResponseEntity<String> changeStatus(
            @PathVariable("partyApplicationId") long partyApplicationId,
            @PathVariable("statusType") PartyApplicationStatusTypeEnum statusType
    ) {

        return ResponseEntity.ok(partyApplicationUseCase.changeStatus(partyApplicationId, statusType));
    }
}