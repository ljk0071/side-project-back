package com.side.rest.domain.resume.controller;

import com.side.rest.domain.resume.dto.request.ResumeRequestDto;
import com.side.security.service.SecurityHelper;
import com.side.usecase.resume.ResumeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.side.rest.mapper.ResumeMapper.ResumeMapper;

@Slf4j
@RestController
@RequestMapping("/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeUseCase resumeUseCase;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ResumeRequestDto resumeRequestDto) {

        resumeRequestDto.setUserUniqueId(SecurityHelper.getAuthenticatedUser().uniqueId());

        resumeUseCase.create(ResumeMapper.toDomain(resumeRequestDto));

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> delete(@PathVariable long resumeId) {

        resumeUseCase.delete(resumeId);

        return ResponseEntity.ok(null);
    }
}