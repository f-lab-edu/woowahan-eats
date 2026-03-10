package com.flab.woowahaneats.domain.notification.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitterManager {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String id, SseEmitter emitter) {
        emitters.put(id, emitter);
        log.info("SSE 연결 저장: {}", id);

        emitter.onCompletion(() -> {
            log.info("SSE 연결 완료: {}", id);
            emitters.remove(id);
        });

        emitter.onTimeout(() -> {
            log.info("SSE 연결 타임아웃: {}", id);
            emitter.complete();
            emitters.remove(id);
        });

        emitter.onError((e) -> {
            log.error("SSE 연결 에러: {}", id, e);
            emitter.completeWithError(e);
            emitters.remove(id);
        });

        return emitter;
    }

    public void sendToRole(String role, Object data) {
        emitters.forEach((id, emitter) -> {
            if (id.startsWith(role + "_")) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(data));
                    log.info("SSE 알림 전송 성공 (role): {}", id);
                } catch (IOException e) {
                    log.error("SSE 알림 전송 실패 (role): {}", id, e);
                    emitter.completeWithError(e);
                    emitters.remove(id);
                }
            }
        });
    }

    public void sendToId(String id, Object data) {
        SseEmitter emitter = emitters.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(data));
                log.info("SSE 알림 전송 성공 (id): {}", id);
            } catch (IOException e) {
                log.error("SSE 알림 전송 실패 (id): {}", id, e);
                emitter.completeWithError(e);
                emitters.remove(id);
            }
        } else {
            log.warn("SSE 연결을 찾을 수 없음: {}", id);
        }
    }

    public void deleteById(String id) {
        emitters.remove(id);
        log.info("SSE 연결 제거: {}", id);
    }
}