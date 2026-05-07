package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.controller.dto.ChatResponse;
import com.flab.woowahaneats.domain.chatbot.retrieval.DataQuery;
import com.flab.woowahaneats.domain.chatbot.retrieval.DataRetriever;
import com.flab.woowahaneats.domain.chatbot.intent.Intent;
import com.flab.woowahaneats.domain.chatbot.intent.IntentClassifier;
import com.flab.woowahaneats.domain.chatbot.retrieval.RetrievalResult;
import com.flab.woowahaneats.domain.restaurant.owner.service.OwnerRestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final OwnerRestaurantService ownerRestaurantService;
    private final IntentClassifier intentClassifier;
    private final List<DataRetriever> dataRetrievers;
    private final AnswerSynthesisService answerSynthesisService;

    private static final String UNSUPPORTED_MESSAGE =
            "죄송합니다. 해당 질문은 현재 지원하지 않습니다.\n\n"
            + "다음과 같은 질문을 해보세요:\n"
            + "- 매출: \"이번 달 매출 얼마야?\", \"일별 매출 추이 알려줘\"\n"
            + "- 주문: \"오늘 주문 몇 건이야?\", \"취소된 주문 몇 건?\"\n"
            + "- 메뉴: \"가장 많이 팔린 메뉴가 뭐야?\", \"치킨 얼마나 팔렸어?\"\n"
            + "- 배달: \"배달 현황 알려줘\", \"평균 배달 시간이 어떻게 돼?\"\n"
            + "- 정책: \"수수료가 어떻게 되나요?\", \"입점 절차 알려줘\"";

    @Override
    public ChatResponse ask(String message) {
        Intent intent = intentClassifier.classify(message);
        if (intent == Intent.UNSUPPORTED) {
            return ChatResponse.of(UNSUPPORTED_MESSAGE);
        }

        Long restaurantId = ownerRestaurantService.getMyRestaurantId();
        DataQuery query = new DataQuery(intent, message, restaurantId);

        RetrievalResult result = findRetriever(query).retrieve(query);
        String answer = answerSynthesisService.synthesize(message, result.data());
        return ChatResponse.of(answer);
    }

    @Override
    public Flux<String> askStream(String message) {
        Intent intent = intentClassifier.classify(message);
        if (intent == Intent.UNSUPPORTED) {
            return Flux.just(UNSUPPORTED_MESSAGE);
        }

        Long restaurantId = ownerRestaurantService.getMyRestaurantId();
        DataQuery query = new DataQuery(intent, message, restaurantId);

        return Flux.defer(() -> {
            RetrievalResult result = findRetriever(query).retrieve(query);
            return answerSynthesisService.synthesizeStream(message, result.data());
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private DataRetriever findRetriever(DataQuery query) {
        return dataRetrievers.stream()
                .filter(r -> r.supports(query))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "지원하는 DataRetriever가 없습니다: " + query.intent()));
    }
}
