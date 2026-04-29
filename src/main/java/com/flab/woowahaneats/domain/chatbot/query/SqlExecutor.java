package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.exception.SqlExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SqlExecutor {

    private final JdbcTemplate jdbcTemplate;

    private static final int MAX_ROWS = 100;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> execute(String sql) {
        try {
            return jdbcTemplate.queryForList(ensureLimit(sql));
        } catch (Exception e) {
            throw new SqlExecutionException("SQL 실행 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String ensureLimit(String sql) {
        if (!sql.toUpperCase().contains("LIMIT")) {
            return sql + " LIMIT " + MAX_ROWS;
        }
        return sql;
    }
}