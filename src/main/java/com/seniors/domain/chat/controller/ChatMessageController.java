package com.seniors.domain.chat.controller;

import com.seniors.common.dto.ErrorResponse;
import com.seniors.domain.chat.dto.ChatMessageDto.ChatMessageTransDto;
import com.seniors.domain.chat.entity.ChatMessage;
import com.seniors.domain.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Tag(name = "채팅 메세지", description = "채팅 메세지 API 명세서")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final SimpMessageSendingOperations template;
    private final ChatMessageService chatMessageService;

    @Operation(summary = "채팅하기")
    @ApiResponse(responseCode = "200", description = "송/수신 성공",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "서버 에러.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @MessageMapping("/chat/sendMessage")
    public void chatMessageSend(@Payload ChatMessageTransDto chat) {

        template.convertAndSend("/sub/chat/room/" + chat.getChatRoomId(), chatMessageService.saveChatMessage(chat));

    }



}
