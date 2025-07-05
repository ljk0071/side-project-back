package com.side.rest.domain.party.controller;

import com.side.rest.domain.party.dto.request.ResumeRequestDto;
import com.side.security.service.SecurityHelper;
import com.side.usecase.resume.ResumeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.side.rest.mapper.ResumeMapper.ResumeMapper;

@Slf4j
@RestController
@RequestMapping("/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeUseCase resumeUseCase;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Validated(ResumeRequestDto.Insert.class) ResumeRequestDto resumeRequestDto
    ) {

        resumeRequestDto.setUserUniqueId(SecurityHelper.getAuthenticatedUser().uniqueId());

        resumeUseCase.create(ResumeMapper.toDomain(resumeRequestDto));

        return ResponseEntity.ok(null);
    }
}