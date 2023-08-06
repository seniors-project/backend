package com.seniors.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.dto.CustomPage;
import com.seniors.common.exception.type.NotAuthorizedException;
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
import com.seniors.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
	private static final long DEFAULT_TIMEOUT = 1000 * 60 * 60 * 24; // 1일

	private final EmitterRepository emitterRepository;
	private final NotificationRepository notificationRepository;
	private final UsersRepository usersRepository;
	private final ObjectMapper objectMapper;

	public SseEmitter notificationSubscribe(Long userId, String lastEventId) {
		String id = userId.toString();

		SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

		emitter.onCompletion(() -> emitterRepository.deleteById(id));
		emitter.onTimeout(() -> emitterRepository.deleteById(id));

		// To prevent 503 error, send a dummy event
//		sendDummyEvent(emitter, userId);

		// Send unsent Event list to prevent Event loss
		sendUnsentEvents(emitter, userId, lastEventId);

		return emitter;
	}

	private void sendDummyEvent(SseEmitter emitter, Long userId) {
		String dummyEvent = "{\"message\": \"EventStream Created. [userId=" + userId + "]\"," +
				" \"url\": \"/api/posts\", \"content\": \"시니어스 구독 userId " + userId + " \", \"id\": " + userId + ", \"isRead\": false, \"createdAt\": \"2023-01-01 01:01:01.123456\"}";
		sendToClient(emitter, userId.toString(), dummyEvent);
	}

	private void sendUnsentEvents(SseEmitter emitter, Long userId, String lastEventId) {
		if (lastEventId != null && !lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
			events.entrySet().stream()
					.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
					.forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
		}
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
						String notificationJson = objectMapper.writeValueAsString(NotificationDto.of(notification));
						emitterRepository.saveEventCache(key, notificationJson);
						sendToClient(emitter, key, notificationJson);
					} catch (JsonProcessingException e) {
						log.error("Error processing JSON:", e);
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

		return Notification.of(receiver, content, url, false);
	}

	public void sendToClient(SseEmitter emitter, String id, Object data) {
		try {
			emitter.send(SseEmitter.event()
					.id(id)
					.name("sse")
					.data(data));
			emitter.complete(); // 리소스 정리를 위해 emitter를 완료합니다
		} catch (IOException exception) {
			log.error("Error sending SSE event:", exception);
			emitterRepository.deleteById(id);
			emitter.complete(); // 리소스 정리를 위해 emitter를 완료합니다
			throw new RuntimeException("연결 오류!");
		}
	}

	@Transactional
	public CustomPage<NotificationDto> findNotificationList(CustomUserDetails userDetails, int size, Long lastId) {
		Sort.Direction direction = Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(0, size, Sort.by(direction, "id"));
		Users users =  usersRepository.findById(userDetails.getUserId()).orElseThrow(
				() -> new NotFoundException("유효하지 않은 회원입니다.")
		);
		Slice<NotificationDto> results = notificationRepository.findNotificationList(users.getId(), pageable, lastId);
		return CustomPage.of(results);
	}

	@Transactional
	public void readNotification(CustomUserDetails userDetails, Long id) {
		Users users = usersRepository.findById(userDetails.getUserId()).orElseThrow(
				() -> new NotFoundException("유효하지 않은 회원입니다.")
		);
		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));

		if (!users.getId().equals(notification.getUsers().getId())) {
			throw new NotAuthorizedException("읽기 권한이 없습니다.");
		}
		notification.read();
	}
}
