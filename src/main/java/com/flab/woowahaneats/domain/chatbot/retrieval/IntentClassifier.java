package com.flab.woowahaneats.domain.chatbot.retrieval;

import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IntentClassifier {

    private final ChatClient chatClient;

    private static final ChatOptions OPTIONS = ChatOptions.builder()
            .temperature(0.0)
            .maxTokens(20)
            .build();

    private static final String SYSTEM_PROMPT = """
            You are an intent classifier for a food delivery platform's owner chatbot.
            Classify the owner's question into exactly ONE of these intents:

            - REVENUE: 매출, 수익, 매출 분석, 총 매출, 수입 관련
            - ORDER: ���문 수, 주문 현황, 주문 건수, 취소 주문 관련
            - MENU: 메뉴별 판매량, 인기 메뉴, 메뉴 분석 관련
            - DELIVERY: 배달 현황, 배달 시간, 라이더, 배달 상태 관련
            - FAQ_POLICY: 입점 절차, 정산, 수수료, 서비스 이용 방법, 정책, 가이드, 계정 관련

            Output ONLY the intent name (e.g., REVENUE). No explanation.
            """;

    public Intent classify(String message) {
        try {
            String result = chatClient
                    .prompt()
                    .system(SYSTEM_PROMPT)
                    .user(message)
                    .options(OPTIONS)
                    .call()
                    .content();

            return parseIntent(result.trim());
        } catch (IllegalArgumentException e) {
            log.warn("Intent 분류 실패, 기본값 REVENUE 사용: {}", e.getMessage());
            return Intent.REVENUE;
        } catch (Exception e) {
            log.error("Intent 분류 중 LLM 호출 실패: {}", e.getMessage(), e);
            throw new LlmCallException("질문 분류 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private Intent parseIntent(String result) {
        String cleaned = result.toUpperCase().replaceAll("[^A-Z_]", "");
        return Intent.valueOf(cleaned);
    }
}
