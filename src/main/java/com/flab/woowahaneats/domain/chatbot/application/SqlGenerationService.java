package com.flab.woowahaneats.domain.chatbot.application;

import com.flab.woowahaneats.domain.chatbot.domain.ChatQuestion;
import com.flab.woowahaneats.domain.chatbot.exception.LlmCallException;
import com.flab.woowahaneats.domain.chatbot.prompt.SchemaLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqlGenerationService {

    private final ChatClient chatClient;
    private final SchemaLoader schemaLoader;

    private static final String SYSTEM_PROMPT = """
            You are a PostgreSQL SQL generator. Output ONLY a single SELECT statement. No explanation, no markdown, no comments.

            {schema}

            RULES:
            - Always include: WHERE restaurant_id = {restaurantId}
            - IMPORTANT: Only user_orders, restaurants, menus, restaurant_operation_infos have restaurant_id column
            - deliveries and user_order_menus do NOT have restaurant_id — always JOIN user_orders first
            - For user_order_menus: JOIN user_orders ON user_order_id, then filter by uo.restaurant_id
            - For deliveries: JOIN user_orders ON order_id = user_order_id, then filter by uo.restaurant_id
            - Exclude CANCELLED orders in revenue/sales queries
            - Order status and cancellation info is in user_orders.status, NOT in deliveries
            - Use PostgreSQL syntax (DATE_TRUNC, EXTRACT, CURRENT_DATE)

            EXAMPLES:
            Q: 이번 달 총 매출?
            A: SELECT SUM(menu_total_price) FROM user_orders WHERE restaurant_id = {restaurantId} AND status != 'CANCELLED' AND created_at >= DATE_TRUNC('month', CURRENT_DATE)

            Q: 가장 많이 팔린 메뉴?
            A: SELECT uom.menu_name, SUM(uom.quantity) AS total_qty FROM user_order_menus uom JOIN user_orders uo ON uom.user_order_id = uo.user_order_id WHERE uo.restaurant_id = {restaurantId} AND uo.status != 'CANCELLED' GROUP BY uom.menu_name ORDER BY total_qty DESC LIMIT 5

            Q: 오늘 주문 몇 건?
            A: SELECT COUNT(*) FROM user_orders WHERE restaurant_id = {restaurantId} AND created_at >= CURRENT_DATE

            Q: 취소된 주문 몇 건?
            A: SELECT COUNT(*) FROM user_orders WHERE restaurant_id = {restaurantId} AND status = 'CANCELLED'
            """;

    private static final String USER_PROMPT = """
            점주 질문: {question}
            """;

    public String generateSql(ChatQuestion question) {
        try {
            String sql = chatClient
                    .prompt()
                    .system(s -> s
                            .text(SYSTEM_PROMPT)
                            .param("schema", schemaLoader.getSchemaSummary())
                            .param("restaurantId", question.restaurantId().toString()))
                    .user(u -> u
                            .text(USER_PROMPT)
                            .param("question", question.message()))
                    .call()
                    .content();

            return stripCodeBlock(sql.trim());
        } catch (Exception e) {
            log.error("SQL 생성 중 LLM 호출 실패: {}", e.getMessage(), e);
            throw new LlmCallException("SQL 생성 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private String stripCodeBlock(String sql) {
        if (sql.startsWith("```")) {
            sql = sql.replaceFirst("```(?:sql)?\\s*\n?", "");
            sql = sql.replaceFirst("\\s*```\\s*$", "");
        }
        sql = sql.replaceAll("--[^\n]*", "");
        sql = sql.replaceAll(";\\s*$", "");
        return sql.trim();
    }
}