# Schema Summary (v1)

## restaurants
음식점 정보. 점주가 등록하고 관리자 승인 후 노출된다.

| column | type | note |
|---|---|---|
| restaurant_id | BIGINT PK | |
| owner_id | BIGINT FK → owners | |
| name | VARCHAR | 음식점 이름 |
| category | VARCHAR | CAFE_DESSERT / FAST_FOOD / KOREAN / CHINESE / SOUP_STEW / BUNSIK / CHICKEN |
| avg_rating | DOUBLE | 평균 평점 |
| approval_status | VARCHAR | PENDING / APPROVED / REJECTED |

## restaurant_operation_infos
음식점 운영 정보. restaurants와 1:1 관계.

| column | type | note |
|---|---|---|
| restaurant_id | BIGINT PK, FK → restaurants | |
| min_order_amt | INT | 최소 주문 금액 (원) |
| delivery_fee | INT | 배달비 (원) |
| open | BOOLEAN | 현재 영업 여부 |

## user_orders
고객 주문 1건. **⚠ 점주 쿼리 시 WHERE restaurant_id 조건 필수.**

| column | type | note |
|---|---|---|
| user_order_id | BIGINT PK | |
| user_id | BIGINT FK → users | |
| restaurant_id | BIGINT FK → restaurants | |
| status | VARCHAR | PENDING → ACCEPTED → COOKING → READY → DELIVERING → COMPLETED / CANCELLED |
| menu_total_price | INT | 메뉴 합계 금액 (원) |
| delivery_fee | INT | 배달비 (원) |
| total_price | INT | menu_total_price + delivery_fee |
| created_at | TIMESTAMP | 주문 생성 시각 |
| completed_at | TIMESTAMP | 완료/취소 시각 (nullable) |

## user_order_menus
주문에 포함된 개별 메뉴 항목. **⚠ user_orders와 JOIN하여 restaurant_id 조건 필수.**

| column | type | note |
|---|---|---|
| user_order_id | BIGINT FK → user_orders | |
| menu_id | BIGINT | 메뉴 ID |
| menu_name | VARCHAR | 주문 시점 메뉴명 스냅샷 |
| price | INT | 주문 시점 단가 (원) |
| quantity | INT | 수량 |

## menus
음식점의 메뉴 목록.

| column | type | note |
|---|---|---|
| menu_id | BIGINT PK | |
| restaurant_id | BIGINT FK → restaurants | |
| display_name | VARCHAR | 고객용 메뉴명 |
| internal_name | VARCHAR | 점주용 내부 메뉴명 |
| price | INT | 현재 판매가 (원) |
| available | BOOLEAN | 판매 가능 여부 |

## deliveries
배달 진행 상태. **⚠ user_orders와 JOIN하여 restaurant_id 조건 필수.**

| column | type | note |
|---|---|---|
| delivery_id | BIGINT PK | |
| order_id | BIGINT FK → user_orders (UNIQUE) | |
| rider_id | BIGINT FK → riders (nullable) | |
| status | VARCHAR | PENDING → ASSIGNED → PICKING_UP → PICKED_UP → IN_DELIVERY → DELIVERED / CANCELLED |
| assigned_at | TIMESTAMP | 라이더 배정 시각 (nullable) |
| picked_up_at | TIMESTAMP | 픽업 완료 시각 (nullable) |
| delivered_at | TIMESTAMP | 배달 완료 시각 (nullable) |
| created_at | TIMESTAMP | 배달 생성 시각 |

---

## 관계 요약

```
restaurants.owner_id             → owners.id                (N:1)
restaurant_operation_infos.restaurant_id → restaurants.restaurant_id (1:1)
user_orders.restaurant_id        → restaurants.restaurant_id (N:1)
user_order_menus.user_order_id   → user_orders.user_order_id (N:1)
menus.restaurant_id              → restaurants.restaurant_id (N:1)
deliveries.order_id              → user_orders.user_order_id (1:1)
```

**자주 쓰이는 JOIN 패턴:**
- 매출 분석: `user_orders JOIN user_order_menus ON user_order_id`
- 메뉴별 판매량: `user_order_menus JOIN menus USING (menu_id)`
- 배달 소요시간: `user_orders JOIN deliveries ON user_order_id = order_id`
- 운영 정보 포함: `restaurants JOIN restaurant_operation_infos USING (restaurant_id)`