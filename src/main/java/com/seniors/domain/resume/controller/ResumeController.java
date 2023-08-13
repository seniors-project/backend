package com.seniors.domain.resume.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.CustomSlice;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.config.security.CustomUserDetails;
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생성 요청 body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResumeDto.SaveResumeReq.class)))
    @ApiResponse(responseCode = "200", description = "등록 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "이미 해당 유저의 이력서가 존재합니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

    @PostMapping("")
    public DataResponseDto<List<String>> resumeAdd(
            @ModelAttribute @Valid ResumeDto.SaveResumeReq resumeDto,
            @LoginUsers CustomUserDetails userDetails
        ) throws IOException {
        resumeService.addResume(resumeDto, userDetails.getUserId());
        return DataResponseDto.of(null);
    }

    @Operation(summary = "이력서 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResumeDto.GetResumeRes.class)))
    @ApiResponse(responseCode = "404", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "이력서가 존재하지 않습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
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
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResumeDto.GetResumeByQueryDslRes.class)))
    @ApiResponse(responseCode = "404", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("")
    public DataResponseDto<CustomSlice<ResumeDto.GetResumeByQueryDslRes>> resumeList(
            @RequestParam int size,
            @RequestParam(required = false) Long lastId,
            @LoginUsers CustomUserDetails userDetails
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return resumeService.findResumeList(pageable, lastId, userDetails.getUserId());
    }

    @Operation(summary = "이력서 수정")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생성 요청 body",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResumeDto.ModifyResumeReq.class)))
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "이력서가 존재하지 않습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "수정 권한이 없습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PatchMapping("/{resumeId}")
    public DataResponseDto<List<String>> resumeModify(
            @PathVariable Long resumeId,
            @ModelAttribute @Valid ResumeDto.ModifyResumeReq resumeDto,
            @LoginUsers CustomUserDetails userDetails
    ) throws IOException {
        resumeService.modifyResume(resumeId, resumeDto, userDetails.getUserId());
        return DataResponseDto.of(null);
    }

    @Operation(summary = "이력서 삭제")
    @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "이력서가 존재하지 않습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "삭제 권한이 없습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{resumeId}")
    public DataResponseDto<Long> resumeRemove(
            @PathVariable Long resumeId,
            @LoginUsers CustomUserDetails userDetails
    ) {
        resumeService.removeResume(resumeId, userDetails.getUserId());
        return DataResponseDto.of(null);
    }

}
