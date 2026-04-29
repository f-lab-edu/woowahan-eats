package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.controller.dto.ChatResponse;
import com.flab.woowahaneats.domain.chatbot.domain.ChatQuestion;
import com.flab.woowahaneats.domain.chatbot.query.SqlExecutor;
import com.flab.woowahaneats.domain.chatbot.query.SqlValidator;
import com.flab.woowahaneats.domain.restaurant.owner.service.OwnerRestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final OwnerRestaurantService ownerRestaurantService;
    private final SqlGenerationService sqlGenerationService;
    private final SqlValidator sqlValidator;
    private final SqlExecutor sqlExecutor;
    private final AnswerSynthesisService answerSynthesisService;

    @Override
    public ChatResponse ask(String message) {
        Long restaurantId = ownerRestaurantService.getMyRestaurantId();
        PipelineResult result = runPipeline(restaurantId, message);
        String answer = answerSynthesisService.synthesize(message, result.rows());
        return ChatResponse.of(answer, result.sql());
    }

    @Override
    public Flux<String> askStream(String message) {
        Long restaurantId = ownerRestaurantService.getMyRestaurantId();

        return Flux.defer(() -> {
            PipelineResult result = runPipeline(restaurantId, message);
            return answerSynthesisService.synthesizeStream(message, result.rows());
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private PipelineResult runPipeline(Long restaurantId, String message) {
        ChatQuestion question = new ChatQuestion(restaurantId, message);

        String sql = sqlGenerationService.generateSql(question);
        log.info("생성된 SQL: {}", sql);

        sql = sqlValidator.validate(sql, restaurantId);
        log.info("검증 후 SQL: {}", sql);

        List<Map<String, Object>> rows = sqlExecutor.execute(sql);
        return new PipelineResult(sql, rows);
    }

    private record PipelineResult(String sql, List<Map<String, Object>> rows) {}
}