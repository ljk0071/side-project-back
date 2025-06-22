package com.side.rest.resume.controller;

import static com.side.rest.mapper.ResumeMapper.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.rest.resume.dto.request.ResumeRequestDto;
import com.side.security.service.SecurityHelper;
import com.side.usecase.resume.ResumeUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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