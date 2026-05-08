package com.flab.woowahaneats.domain.chatbot.query.validation;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class SqlValidator {

    private static final Set<String> ALLOWED_TABLES = Set.of(
            "user_orders", "user_order_menus", "menus", "deliveries"
    );

    public ValidationResult validate(String sql) {
        if (sql == null || sql.isBlank()) {
            return ValidationResult.fail("SQL이 비어있습니다.");
        }

        // 다중 statement 차단
        if (sql.contains(";")) {
            return ValidationResult.fail("다중 SQL 문은 허용되지 않습니다.");
        }

        // UNION 차단 (복합 쿼리 금지)
        if (sql.toUpperCase().contains("UNION")) {
            return ValidationResult.fail("UNION 쿼리는 허용되지 않습니다.");
        }

        if (!sql.contains("restaurant_id")) {
            return ValidationResult.fail("restaurant_id 필터가 누락되었습니다.");
        }

        try {
            String parsableSql = sql.replaceAll(":([a-zA-Z_][a-zA-Z0-9_]*)", "0");
            Statement statement = CCJSqlParserUtil.parse(parsableSql);

            // SELECT만 허용
            if (!(statement instanceof Select)) {
                return ValidationResult.fail("SELECT 문만 허용됩니다.");
            }

            // 테이블 화이트리스트 검증
            TablesNamesFinder finder = new TablesNamesFinder();
            List<String> tables = finder.getTableList(statement);
            for (String table : tables) {
                if (!ALLOWED_TABLES.contains(table.toLowerCase())) {
                    return ValidationResult.fail("허용되지 않은 테이블: " + table);
                }
            }

            return ValidationResult.ok();
        } catch (Exception e) {
            return ValidationResult.fail("SQL 파싱 실패: " + e.getMessage());
        }
    }

    public record ValidationResult(boolean valid, String errorMessage) {
        static ValidationResult ok() {
            return new ValidationResult(true, null);
        }

        static ValidationResult fail(String message) {
            return new ValidationResult(false, message);
        }
    }
}
