package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * SQL 실행 결과를 자연어 답변으로 합성하는 서비스.
 * 두 가지 입력을 지원:
 * - String: 이미 포맷팅된 결과 텍스트 (DataRetriever에서 전달)
 * - List<Map<String, Object>>: raw SQL 결과 (레거시 호환)
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerSynthesisService {

    private final ChatClient chatClient;

    private static final ChatOptions SYNTHESIS_OPTIONS = ChatOptions.builder()
            .temperature(0.3)
            .maxTokens(1024)
            .build();

    private static final String SYSTEM_PROMPT = """
            너는 음식 배달 플랫폼 "woowahan-eats"의 점주용 데이터 분석 어시스턴트다.
            SQL 실행 결과를 점주에게 자연스러운 한국어 문장으로 답변하라.

            ## 규칙
            1. 반드시 자연스러운 문장으로 답변하라. 표(테이블)나 목록 형태로 답하지 마라.
            2. 숫자에는 천 단위 구분자(,)를 사용하고, 금액에는 "원"을 붙여라.
            3. 데이터가 없으면 "해당 기간에 데이터가 없습니다."라고 답하라.
            4. 간결하게 2~3문장 이내로 답하라.

            ## 답변 예시
            - 질문: 가장 많이 팔린 메뉴? → "가장 많이 팔린 메뉴는 양념 치킨으로 총 15개 판매되었습니다. 그 다음은 후라이드 치킨 12개, 반반 치킨 7개 순입니다."
            - 질문: 이번 달 매출? → "이번 달 총 매출은 578,000원입니다."
            - 질문: 오늘 주문 몇 건? → "오늘 주문은 총 5건입니다."
            """;

    private static final String USER_PROMPT = """
            점주 질문: {question}

            SQL 실행 결과:
            {result}
            """;

    public String synthesize(String question, String resultText) {
        try {
            return chatClient
                    .prompt()
                    .system(SYSTEM_PROMPT)
                    .user(u -> u
                            .text(USER_PROMPT)
                            .param("question", question)
                            .param("result", resultText))
                    .options(SYNTHESIS_OPTIONS)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("답변 합성 중 LLM 호출 실패: {}", e.getMessage(), e);
            throw new LlmCallException("답변 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    public Flux<String> synthesizeStream(String question, String resultText) {
        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(u -> u
                        .text(USER_PROMPT)
                        .param("question", question)
                        .param("result", resultText))
                .options(SYNTHESIS_OPTIONS)
                .stream()
                .content()
                .onErrorResume(e -> {
                    log.error("스트리밍 답변 합성 중 LLM 호출 실패: {}", e.getMessage(), e);
                    return Flux.error(new LlmCallException(
                            "답변 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
                });
    }

}