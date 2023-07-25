package com.seniors.domain.resume.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Tag(name = "이력서", description = "이력서 API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "이력서 등록")
    @ApiResponse(responseCode = "200", description = "등록 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @PostMapping("")
    public DataResponseDto<List<String>> resumeAdd(
            @ModelAttribute @Valid ResumeDto.SaveResumeReq resumeDto, BindingResult bindingResult,
            @LoginUsers CustomUserDetails userDetails
        ) throws IOException {
        return DataResponseDto.of(resumeService.addResume(resumeDto, bindingResult, userDetails.getUserId()));
    }

    @Operation(summary = "이력서 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @GetMapping("/{resumeId}")
    public DataResponseDto<ResumeDto.GetResumeRes> resumeDetails(
            @PathVariable Long resumeId,
            @LoginUsers CustomUserDetails userDetails
    ) {
        ResumeDto.GetResumeRes getResumeRes = resumeService.findResume(resumeId, userDetails.getUserId());
        return DataResponseDto.of(getResumeRes);
    }


    @Operation(summary = "이력서 리스트 조회")
    @ApiResponse(responseCode = "200", description = "리스트 조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @GetMapping("")
    public DataResponseDto<Slice<ResumeDto.GetResumeByQueryDslRes>> resumeList(
            @RequestParam int size,
            @RequestParam Long lastId,
            @LoginUsers CustomUserDetails userDetails
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return resumeService.findResumeList(pageable, lastId, userDetails.getUserId());
    }
}
