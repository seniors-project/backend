package com.seniors.domain.chat.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.CustomPage;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.chat.service.ChatRoomService;
import com.seniors.domain.users.dto.UsersDto.GetChatUserRes;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 생성")
    @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @PostMapping("")
    public DataResponseDto createRoom(
            @RequestBody ChatRoomDto.ChatRoomCreateDto chatRoomCreateDto,
            @LoginUsers CustomUserDetails userDetails) {

        chatRoomService.addChatRoom(chatRoomCreateDto.getChatUserId(), chatRoomCreateDto.getRoomName(), userDetails.getUserId());

        return DataResponseDto.of("SUCCESS");
    }

    @Operation(summary = "채팅방 전체 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @GetMapping("")
    public DataResponseDto<CustomPage<GetChatUserRes>> chatRoomList (
            @LoginUsers CustomUserDetails userDetails,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) int size) {

        CustomPage<GetChatUserRes> chatRoomResList = chatRoomService.findChatRoom(userDetails.getUserId(), page > 0 ? page - 1 : 0, size);

        return DataResponseDto.of(chatRoomResList);
    }

    @Operation(summary = "채팅방 입장")
    @ApiResponse(responseCode = "200", description = "입장 성공",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
    @GetMapping("{roomId}")
    public DataResponseDto<ChatRoomDto.GetChatRoomRes> chatRoom (
//            @LoginUsers CustomUserDetails userDetails,
            @PathVariable Long roomId) {

        ChatRoomDto.GetChatRoomRes getChatRoomRes = chatRoomService.findOneChatRoom(roomId);

        return DataResponseDto.of(getChatRoomRes);
    }

}
