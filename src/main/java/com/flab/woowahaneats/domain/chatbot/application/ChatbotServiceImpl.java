package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.controller.dto.ChatResponse;
import com.flab.woowahaneats.domain.chatbot.retrieval.DataQuery;
import com.flab.woowahaneats.domain.chatbot.retrieval.DataRetriever;
import com.flab.woowahaneats.domain.chatbot.retrieval.IntentClassifier;
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

    @Override
    public ChatResponse ask(String message) {
        Long restaurantId = ownerRestaurantService.getMyRestaurantId();
        DataQuery query = new DataQuery(intentClassifier.classify(message), message, restaurantId);

        RetrievalResult result = findRetriever(query).retrieve(query);
        String answer = answerSynthesisService.synthesize(message, result.data());
        return ChatResponse.of(answer, result.source());
    }

    @Override
    public Flux<String> askStream(String message) {
        Long restaurantId = ownerRestaurantService.getMyRestaurantId();
        DataQuery query = new DataQuery(intentClassifier.classify(message), message, restaurantId);

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
