# ADR-001: 점주용 챗봇 아키텍처 재설계

- **상태**: 제안 (Proposed)
- **일자**: 2026-05-05
- **작성 배경**: 멘토 코드 리뷰 피드백 + 우아콘2025 RAG 발표 참고

---

## 1. 문제 인식

### 멘토님 피드백 요약

현재 Text-to-SQL 구현에 대해 다음 지적을 받음:

1. **설계 부실** — "왜 SQL을 생성해야 하는지, 추상화 레벨을 왜 이렇게 설정해야 하는지 타당성 논의가 없다"
2. **구색 맞추기** — "AI를 서비스에 적용해봤습니다 한 마디 하려고 구색 맞추기용 구현으로 밖에 안 보인다"
3. **추상화 문제** — "챗봇이 SQL이라는 거에 직접 의존하는 형태. SQL 변경 시 챗봇 로직도 바뀌어야 한다. 설계 원칙이 하나도 안 들어간 것"
4. **확장 불가** — "추후 퀄리티를 계속 끌어올릴 수 있는 구조가 아니다"

### 현재 구현의 근본적 한계

```
질문 → LLM이 SQL 직접 생성 → DB 실행 → LLM이 답변 생성
```

- 챗봇이 **SQL이라는 구체 기술에 직접 의존** (DIP 위반)
- 프롬프트가 **코드에 하드코딩** — 프롬프트 관리 책임과 실행 책임이 분리되지 않음
- 데이터 검색 전략을 바꾸면 **챗봇 전체가 영향** (OCP 위반)
- LLM의 비결정적(Non-deterministic) 특성에 대한 **실행 안정성 고려 없음**

---

## 2. 핵심 설계 질문들

### Q1. 챗봇에서 SQL이 바로 드러나야 하는가?

**아니오.** 챗봇이 의존해야 하는 추상성은 "매장 관련 데이터를 찾아다 준다"이지, "SQL 쿼리를 만든다"가 아니다.

- 챗봇은 **"필요한 데이터를 요청"** 하고
- 그 데이터를 **"어떻게 가져올지"** 는 별도 계층이 결정

### Q2. RAG가 필요한가?

**부분적으로 필요하다.** 데이터 특성에 따라 기술 선택이 달라져야 한다.

| 데이터 유형 | 예시 | 적합한 접근 |
|------------|------|-----------|
| 정형 데이터 (DB) | 매출, 주문 수, 인기 메뉴, 배달 현황 | Function Calling → 사전 정의된 쿼리 |
| 비정형 데이터 | 서비스 이용 가이드, 정책/수수료 안내, FAQ | RAG (벡터 검색 → LLM 생성) |

**비정형 데이터 소스:** [노션 공개 문서](https://festive-geography-f89.notion.site/Woowahan-Eats-34808b8b68b980289ca5daf3549edb37)
- Part 1. 서비스 이용 가이드 (입점 절차, 매장/메뉴 등록, 배달 설정, 사장님앱)
- Part 2. 정책 및 수수료 (입점비, 광고, 결제 수수료, 배달비, 정산, 환불, 페널티)
- Part 3. FAQ 23개 (입점/메뉴/주문/정산/광고/시스템)

**근거 (우아콘2025 RAG 발표 참고):**
- RAG 필요성 판단 4가지 질문 중, 우리 정형 데이터는 "벡터 유사도 검색"이 아니라 "SQL 집계 연산"이 답
- 발표에서도 "굳이 벡터 DB일 필요는 없다. RDBMS에서 잘 쿼리하는 형태로만 잘 동작하면 된다"고 언급
- 비정형 문서(FAQ, 가이드, 정책)에는 RAG가 적합 — 노션 문서가 이에 해당

### Q3. 벡터 DB가 반드시 필요한가?

**지금은 아니다.** 멘토님 피드백: "굳이 벡터 DB일 필요는 없다. 내가 필요한 데이터를 잘 저장하고 잘 찾을 수 있는 형태면 뭐든 상관없다."

현재 핵심 유스케이스(매출/주문 분석)는 RDBMS 쿼리로 충분하다. 비정형 데이터 지원이 필요해지는 시점에 벡터 DB를 도입한다.

---

## 3. 재설계 방향

### 아키텍처: Intent 분류 + 데이터 검색 추상화 + RAG 확장 가능 구조

```
점주 질문
  → [Intent 분류] LLM이 의도 파악 + 파라미터 추출
      ├─ 매출 분석 → DataRetriever(추상) → StructuredDataRetriever(RDBMS)
      ├─ 주문 현황 → DataRetriever(추상) → StructuredDataRetriever(RDBMS)  
      ├─ 메뉴 분석 → DataRetriever(추상) → StructuredDataRetriever(RDBMS)
      └─ FAQ/가이드 → DataRetriever(추상) → UnstructuredDataRetriever(RAG) [향후]
  → [답변 생성] 검색 결과 + 원본 질문 → LLM → 자연어 응답
```

### 핵심 설계 원칙

#### 원칙 1: 데이터 검색 추상화 (DIP)

챗봇은 "데이터를 가져온다"는 추상에만 의존. SQL/벡터검색 등 구체 기술은 숨긴다.

```java
// 챗봇이 의존하는 추상 — 데이터 검색
interface DataRetriever {
    RetrievalResult retrieve(DataQuery query);
}

// 정형 데이터: StructuredDataRetriever → QueryExecutor(추상) → 실행
class StructuredDataRetriever implements DataRetriever { ... }

// 쿼리 실행 추상 — SQL인지, API 호출인지 Retriever는 모름
interface QueryExecutor {
    List<Map<String, Object>> execute(Intent intent, String templateName,
                                       Long restaurantId, QueryParameters params);
}

// 비정형 데이터: RAG 기반 (향후 확장)
class UnstructuredDataRetriever implements DataRetriever { ... }
```

**이점:**
- `DataRetriever` 레벨: SQL에서 다른 기술로 바꿔도 챗봇 로직 변경 없음 (OCP)
- `QueryExecutor` 레벨: `StructuredDataRetriever`도 SQL 문자열을 직접 다루지 않음 (DIP). 쿼리 바인딩/실행 방식 변경 시 `TemplateQueryExecutor`만 수정

#### 원칙 2: 프롬프트 관리와 실행 분리 (SRP)

멘토님: "프롬프트를 관리하는 책임과 실행하는 책임이 나뉘어야 한다"

```java
// 프롬프트 관리: 외부 파일(YAML)에서 로드, 버전 관리
interface PromptProvider {
    Prompt getPrompt(String promptId, Map<String, Object> variables);
}

// 프롬프트 실행: 네트워크 오류, Rate Limit, 포맷 불일치 등 처리
interface LlmExecutor {
    LlmResponse execute(Prompt prompt, LlmOptions options);
}
```

**이점:**
- 프롬프트만 수정하고 기능 코드는 그대로 둘 수 있음
- LLM API 변경 시 Executor만 교체
- 프롬프트 A/B 테스트, 버전별 비교 가능

#### 원칙 3: LLM 실행 안정성 (비결정적 특성 대응)

멘토님: "LLM은 확률적으로 동작. 정해진 포맷으로 대답 안 할 가능성이 크다"

실행 계층에서 처리할 사항:
- **네트워크 오류** → 재시도 (exponential backoff)
- **Rate Limit** → 큐잉 또는 대기
- **포맷 불일치** → 파싱 실패 시 재요청 또는 fallback
- **토큰 소비 추적** → 비용 대비 아웃풋 모니터링

#### 원칙 4: 데이터 정제와 증강 (색인 파이프라인)

멘토님: "정규화된 데이터를 미리 조인해서 관련 있는 것끼리 묶어 놓으면 LLM에게 전달했을 때 의미가 더 잘 전달된다"

- 원본 데이터: 정규화 유지 (무결성)
- LLM용 데이터: 미리 조인/집계하여 의미 단위로 구성
  - 예: 일별/주별/월별 매출 요약 테이블
  - 예: 메뉴별 판매 순위 + 트렌드
- **100번 요청이 와도 이미 만들어진 데이터를 전달** — 매번 생성하지 않음

---

## 4. 구현 계획 (단계별)

### Phase 1: 구조 재설계 (현재 → 추상화 도입)

**목표:** 챗봇이 SQL에 직접 의존하지 않는 구조로 전환

1. `DataRetriever` 인터페이스 정의
2. Intent 분류 로직 구현 (LLM 기반)
3. `StructuredDataRetriever` 구현 — 사전 정의된 쿼리 템플릿 기반
4. 프롬프트 외부 파일(YAML) 분리
5. `LlmExecutor` 구현 — 실행 안정성 처리

### Phase 2: 데이터 품질 강화 (정제 + 증강)

**목표:** LLM에게 더 좋은 데이터를 제공

1. 주요 분석 데이터 사전 집계 (일별/주별/월별 요약)
2. 메타데이터 부착 (기간, 카테고리, 트렌드 방향)
3. LLM 기반 부가 인사이트 사전 생성 (선택)

### Phase 3: RAG 도입 (비정형 데이터 지원)

**목표:** 노션 점주용 문서(가이드/정책/FAQ)에 대한 자연어 질의 지원

**데이터 소스:** https://festive-geography-f89.notion.site/Woowahan-Eats-34808b8b68b980289ca5daf3549edb37

1. `UnstructuredDataRetriever` 구현
2. 노션 문서 로딩 (노션 API 또는 공개 페이지 크롤링)
3. 문서 색인 파이프라인 (로딩 → 청킹 → 임베딩 → 저장)
   - 청킹 전략: 섹션(Part/항목) 단위 분할 — 의미 경계를 유지
   - 메타데이터: 카테고리(가이드/정책/FAQ), 섹션명, 최종 수정일
4. 벡터 저장소 선택 (Redis Vector Store 또는 pgvector)
5. 평가 파이프라인 (컨텍스트 관련성, 답변 충실성, 답변 관련성)
6. 노션 문서 업데이트 시 색인 갱신 전략

### Phase 4: 고도화 (선택)

- 에이전트 구조 전환 (멘토님: "더 확장하면 에이전트 레벨로 올려야")
- MCP 활용 검토 (데이터 소스 분리)
- 프롬프트 A/B 테스트 + 토큰 비용 추적
- LLM API별 프롬프트 최적화 실험

---

## 5. 면접 관점에서의 가치

이 설계를 통해 설명할 수 있는 것들:

1. **기술 선택의 타당성** — "데이터 특성(정형/비정형)에 따라 접근 방식을 구분했다"
2. **설계 원칙 적용** — DIP(데이터 검색 추상화), SRP(프롬프트 관리/실행 분리), OCP(새 데이터 소스 추가 시 기존 코드 변경 없음)
3. **LLM 특성 이해** — 비결정적 특성 대응, 프롬프트 버저닝, 토큰 비용 관리
4. **RAG 개념 이해** — 색인-생성-평가 파이프라인, 벡터 DB가 항상 답은 아닌 이유
5. **데이터 엔지니어링 관점** — 원본 데이터 정규화 vs LLM용 데이터 증강의 분리

---

## 6. 참고 자료

- [우아콘2025] RAG, 들어는 봤는데... 내 서비스엔 어떻게 쓰지? (김태정, 우아한형제들 교육팀)
  - 블로그: https://techblog.woowahan.com/25900/
  - 영상: https://youtu.be/dP2wliNlvuo
- 멘토 피드백 (2026-05-05): 설계 타당성, 추상화, 프롬프트 관리, 데이터 증강에 대한 방향 제시
