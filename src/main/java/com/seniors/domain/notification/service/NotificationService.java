package com.seniors.domain.notification.service;

import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.notification.dto.NotificationDto;
import com.seniors.domain.notification.entity.Notification;
import com.seniors.domain.notification.repository.EmitterRepository;
import com.seniors.domain.notification.repository.NotificationRepository;
import com.seniors.domain.post.entity.PostLike;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.users.entity.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	private final EmitterRepository emitterRepository;
	private final NotificationRepository notificationRepository;

	public SseEmitter subscribe(Long userId, String lastEventId) {
//		Long userId = user.getUserId();
		// 1
		String id = userId + "_" + System.currentTimeMillis();

		// 2
		SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

		emitter.onCompletion(() -> emitterRepository.deleteById(id));
		emitter.onTimeout(() -> emitterRepository.deleteById(id));

		// 3
		// 503 에러를 방지하기 위한 더미 이벤트 전송
		sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

		// 4
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
	public void send(Users receiver, Comment entity, String content) {
		Notification notification = createNotification(receiver, entity, content);
		String id = String.valueOf(receiver.getId());
		notificationRepository.save(notification);

		Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
		sseEmitters.forEach(
				(key, emitter) -> {
					emitterRepository.saveEventCache(key, notification);
					sendToClient(emitter, key, NotificationDto.from(notification));
				}
		);
	}

	private Notification createNotification(Users receiver, Comment entity, String content) {
		String url = "/api";

//		if (entity instanceof PostLike) {
//			url += "/posts/like/" + ((PostLike) entity).getPost().getId();
//		} else
			if (entity != null) {
			url += "/comments?postId=" + entity.getPost().getId();
		}
//			else if (entity instanceof Resume) {
//			url += "/resumes/" + ((Resume) entity).getId();
//		}

		return Notification.builder()
				.users(receiver)
				.comment(entity)
				.content(content)
				.url(url)
				.isRead(false)
				.build();
	}

	private void sendToClient(SseEmitter emitter, String id, Object data) {
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
}
