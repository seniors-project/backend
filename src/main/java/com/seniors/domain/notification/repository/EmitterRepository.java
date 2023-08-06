package com.seniors.domain.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class EmitterRepository {

	public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	public SseEmitter save(String id, SseEmitter sseEmitter) {
		emitters.put(id, sseEmitter);
		// Broken Pipe 발생시
		sseEmitter.onError(throwable -> {
			log.error("[SSE] - ★★★★★★★★SseEmitters 파일 save 메서드 / [ onError ]");
			log.error("", throwable); // 덕분에 Broken Pipe 에러를 찾을 수 있었음
			sseEmitter.complete();
		});

		// 타임아웃 발생시 콜백 등록
		// complete()이 실행되면 SSE연결 disconnect해주며 onCompletion() 이 호출시킴
		sseEmitter.onTimeout(sseEmitter::complete);

		// 비동기요청 완료시 emitter 객체 삭제
		sseEmitter.onCompletion(() -> {
			emitters.remove(sseEmitter);
		});

		return sseEmitter;
	}

	public void saveEventCache(String id, Object event) {
		eventCache.put(id, event);
	}

	public Map<String, SseEmitter> findAllStartWithById(String id) {
		return emitters.entrySet().stream()
				.filter(entry -> entry.getKey().startsWith(id))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, Object> findAllEventCacheStartWithId(String id) {
		return eventCache.entrySet().stream()
				.filter(entry -> entry.getKey().startsWith(id))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public void deleteAllStartWithId(String id) {
		emitters.forEach(
				(key, emitter) -> {
					if (key.startsWith(id)) {
						emitters.remove(key);
					}
				}
		);
	}

	public void deleteById(String id) {
		emitters.remove(id);
	}

	public void deleteAllEventCacheStartWithId(String id) {
		eventCache.forEach(
				(key, data) -> {
					if (key.startsWith(id)) {
						eventCache.remove(key);
					}
				}
		);
	}
}