package com.seniors.domain.chat.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.chat.service.ChatRoomService;
import com.seniors.domain.users.dto.UsersDto.GetChatUserRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채팅방", description = "채팅방 API 명세서")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @Operation(summary = "채팅방 전체 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetChatUserRes.class)))
    @GetMapping("")
    public DataResponseDto<GetChatUserRes> chatRoomList (
            @Parameter(hidden = true) @LoginUsers CustomUserDetails userDetails) {
        GetChatUserRes getChatUserRes = chatRoomService.findChatRoom(userDetails.getUserId());

        return DataResponseDto.of(getChatUserRes);
    }

    @Operation(summary = "채팅방 생성")
    @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("")
    public DataResponseDto<ChatRoomDto.GetChatRoomRes> chatRoomAdd(
            @RequestBody ChatRoomDto.ChatRoomCreateDto chatRoomCreateDto,
            @LoginUsers CustomUserDetails userDetails) {

        ChatRoomDto.GetChatRoomRes getChatRoomRes = chatRoomService.addChatRoom(userDetails.getUserId(), chatRoomCreateDto.getChatUserId());

        return DataResponseDto.of(getChatRoomRes);
    }
    @Operation(summary = "채팅방 입장")
    @ApiResponse(responseCode = "200", description = "입장 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatRoomDto.GetChatRoomRes.class)))
    @GetMapping("/{roomId}")
    public DataResponseDto<ChatRoomDto.GetChatRoomRes> chatRoomEnter (
            @PathVariable Long roomId,
            @LoginUsers CustomUserDetails userDetails) {

        ChatRoomDto.GetChatRoomRes getChatRoomRes = chatRoomService.findOneChatRoom(roomId, userDetails.getUserId());

        return DataResponseDto.of(getChatRoomRes);
    }
    @Operation(summary = "채팅방 나가기")
    @ApiResponse(responseCode = "200", description = "나가기 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "채팅방이 존재하지 않거나 유효하지 않은 회원입니다",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{roomId}")
    public DataResponseDto<Long> chatRoomExit (
            @PathVariable Long roomId,
            @LoginUsers CustomUserDetails userDetails
    ) {

        chatRoomService.removeChatRoom(roomId, userDetails.getUserId());

        return DataResponseDto.of(null);
    }

}
