package com.flab.woowahaneats.domain.chatbot.query;

import com.flab.woowahaneats.domain.chatbot.exception.InvalidGeneratedSqlException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
public class SqlValidator {

    private static final Set<String> ALLOWED_TABLES = Set.of(
            "restaurants",
            "restaurant_operation_infos",
            "user_orders",
            "user_order_menus",
            "menus",
            "deliveries"
    );

    private static final Pattern DANGEROUS_KEYWORDS = Pattern.compile(
            "\\b(INSERT|UPDATE|DELETE|DROP|ALTER|CREATE|TRUNCATE|REPLACE|MERGE|GRANT|REVOKE|EXEC|EXECUTE|CALL|UNION)\\b",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern SUBQUERY_PATTERN = Pattern.compile(
            "\\bSELECT\\b.*\\bSELECT\\b",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    private static final Pattern TABLE_REFERENCE_PATTERN = Pattern.compile(
            "\\b(?:FROM|JOIN)\\s+([a-z_][a-z0-9_]*)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern FUNCTION_FROM_PATTERN = Pattern.compile(
            "\\b(?:EXTRACT|DATE_PART)\\s*\\([^)]*\\)",
            Pattern.CASE_INSENSITIVE
    );

    public String validate(String sql, Long restaurantId) {
        validateNotEmpty(sql);
        validateSelectOnly(sql);
        validateNoDangerousKeywords(sql);
        validateNoSubquery(sql);
        sql = replaceRestaurantIdPlaceholders(sql, restaurantId);
        sql = ensureRestaurantIdCondition(sql, restaurantId);
        validateAllowedTables(sql);
        return sql;
    }

    private void validateNotEmpty(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new InvalidGeneratedSqlException("SQL이 생성되지 않았습니다.");
        }
    }

    private void validateSelectOnly(String sql) {
        String trimmed = sql.trim().toUpperCase();
        if (!trimmed.startsWith("SELECT")) {
            throw new InvalidGeneratedSqlException("SELECT 문만 실행할 수 있습니다.");
        }
    }

    private void validateNoDangerousKeywords(String sql) {
        if (DANGEROUS_KEYWORDS.matcher(sql).find()) {
            throw new InvalidGeneratedSqlException("허용되지 않는 SQL 키워드가 포함되어 있습니다.");
        }
    }

    private void validateNoSubquery(String sql) {
        if (SUBQUERY_PATTERN.matcher(sql).find()) {
            throw new InvalidGeneratedSqlException("서브쿼리는 허용되지 않습니다.");
        }
    }

    private String ensureRestaurantIdCondition(String sql, Long restaurantId) {
        String idStr = restaurantId.toString();

        Pattern pattern = Pattern.compile(
                "\\brestaurant_id\\s*=\\s*" + idStr + "\\b",
                Pattern.CASE_INSENSITIVE
        );

        if (!pattern.matcher(sql).find()) {
            if (Pattern.compile("\\bWHERE\\b", Pattern.CASE_INSENSITIVE).matcher(sql).find()) {
                sql = sql.replaceFirst("(?i)\\bWHERE\\b", "WHERE restaurant_id = " + idStr + " AND");
            } else {
                sql = sql.replaceFirst("(?i)(FROM\\s+[a-z_][a-z0-9_]*(\\s+[a-z_][a-z0-9_]*)?)", "$1 WHERE restaurant_id = " + idStr);
            }
        }

        return sql;
    }

    private void validateAllowedTables(String sql) {
        String cleaned = FUNCTION_FROM_PATTERN.matcher(sql).replaceAll("");
        var matcher = TABLE_REFERENCE_PATTERN.matcher(cleaned);
        while (matcher.find()) {
            String tableName = matcher.group(1).toLowerCase();
            if (!ALLOWED_TABLES.contains(tableName)) {
                throw new InvalidGeneratedSqlException(
                        "허용되지 않는 테이블입니다: " + tableName
                );
            }
        }
    }

    private String replaceRestaurantIdPlaceholders(String sql, Long restaurantId) {
        return sql
                .replace(":restaurantId", restaurantId.toString())
                .replace("{restaurantId}", restaurantId.toString());
    }
}