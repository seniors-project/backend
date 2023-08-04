package com.seniors.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.dto.CustomPage;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.notification.dto.NotificationDto;
import com.seniors.domain.notification.entity.Notification;
import com.seniors.domain.notification.repository.EmitterRepository;
import com.seniors.domain.notification.repository.NotificationRepository;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.users.entity.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	private final EmitterRepository emitterRepository;
	private final NotificationRepository notificationRepository;
	private final ObjectMapper objectMapper;

	public SseEmitter subscribe(Long userId, String lastEventId) {
		String id = userId.toString();

		SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

		emitter.onCompletion(() -> emitterRepository.deleteById(id));
		emitter.onTimeout(() -> emitterRepository.deleteById(id));

		// 503 에러를 방지하기 위한 더미 이벤트 전송
//		sendToClient(emitter, id, "{\"message\": \"EventStream Created. [userId=" + userId + "]\"," +
//				" \"url\": \"/api/posts\", \"content\": \"시니어스 구독 userId " + userId + " \", \"id\": " + userId + ", \"isRead\": false, \"createdAt\": \"2023-08-02 15:15:40.648816\"}");

		// 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
		if (!lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
			events.entrySet().stream()
					.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
					.forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
		}

		return emitter;
	}

	@Transactional
	public void send(Users receiver, Object entity, String content) {
		Notification notification = createNotification(receiver, entity, content);
		notificationRepository.save(notification);

		String id = String.valueOf(receiver.getId());

		Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
		sseEmitters.forEach(
				(key, emitter) -> {
					try {
						emitterRepository.saveEventCache(key, objectMapper.writeValueAsString(NotificationDto.of(notification)));
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
					try {
						sendToClient(emitter, key, objectMapper.writeValueAsString(NotificationDto.of(notification)));
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				}
		);

	}

	private Notification createNotification(Users receiver, Object entity, String content) {
		String url = "/api";

		if (entity instanceof Post) {
			url += "/posts/" + ((Post) entity).getId();
		} else if (entity instanceof Comment) {
			url += "/posts/" +((Comment) entity).getPost().getId();
		} else if (entity instanceof Resume) {
			url += "/resumes/" + ((Resume) entity).getId();
		}

		return Notification.of(receiver, content, url);
	}

	public void sendToClient(SseEmitter emitter, String id, Object data) {
		try {
			emitter.send(SseEmitter.event()
					.id(id)
					.name("sse")
					.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(id);
			throw new RuntimeException("연결 오류!");
		}
	}

//	@Transactional
	public CustomPage<NotificationDto> findAllById(CustomUserDetails userDetails, int page, int size) {
		Sort.Direction direction = Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
		Page<NotificationDto> responses = notificationRepository.findAllByUsersId(userDetails.getUserId(), pageable);
		return CustomPage.of(responses);
	}

	@Transactional
	public void readNotification(Long id) {
		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));
		notification.read();
	}
}
