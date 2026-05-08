package com.flab.woowahaneats.domain.chatbot.application;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatbotMessages {

    public static final String UNSUPPORTED =
            "죄송합니다. 해당 질문은 현재 지원하지 않습니다.\n\n"
            + "다음과 같은 질문을 해보세요:\n"
            + "- 매출: \"이번 달 매출 얼마야?\", \"일별 매출 추이 알려줘\"\n"
            + "- 주문: \"오늘 주문 몇 건이야?\", \"취소된 주문 몇 건?\"\n"
            + "- 메뉴: \"가장 많이 팔린 메뉴가 뭐야?\", \"치킨 얼마나 팔렸어?\"\n"
            + "- 배달: \"배달 현황 알려줘\", \"평균 배달 시간이 어떻게 돼?\"\n"
            + "- 정책: \"수수료가 어떻게 되나요?\", \"입점 절차 알려줘\"";

    public static final String EMPTY_RESULT = "(결과 없음)";
}
