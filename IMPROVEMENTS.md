# 개선 목록

AI/수동 작업 시 이 문서를 기준으로 진행하면 됩니다. 완료한 항목은 `[x]`로 표시하거나 "완료" 섹션으로 옮기세요.

Last updated: 2025-03

---

## 할 일

- [x] **백엔드 – AnyMealService 생성자**: `private` 무인자 생성자만 있어 Spring 주입과 맞는지 확인. 필요하면 `@Autowired` 생성자 또는 public/package-private 생성자로 정리.
- [x] **백엔드 – 테스트 웹훅 로깅**: `AnyMealController`의 `POST /test/webhook`에서 `System.out.println` 제거 후 `log.debug` 또는 `log.info`로 변경.
- [x] **API – 빈 추천 응답 처리**: `GET /restaurant/near`가 추천 없을 때(Optional empty) 응답 규칙 정하기(204/404 또는 빈 JSON). README 또는 API 설명에 명시.
- [x] **프론트 – document.write 제거**: `index.html`의 `process()`에서 `document.write` 대신 결과를 넣을 `div`를 두고 `textContent`/`innerHTML`로 갱신하도록 변경.
- [x] **프론트 – 빈/부분 응답 처리**: `place_name`이 없거나 응답이 비어 있을 때 "추천할 식당이 없어요" 등 안내 문구 표시.
- [x] **프론트 – 위치 실패 안내**: 좌표를 못 가져와 기본 좌표로 요청할 때, "위치를 사용할 수 없어 기본 지역으로 추천합니다" 같은 문구 노출.
- [x] **문서 – README 보강**: 실행 방법(포트 8080, `anymeal.kakao-restapi-key` 환경 변수), 제공 API(`GET /restaurant/near`) 한두 줄 추가.
- [x] **테스트 – 서비스/API 검증**: Kakao 호출 모킹 후 `AnyMealService#getRestaurantNear` 또는 `GET /restaurant/near` 통합 테스트 추가 (좌표 입력 → Restaurant 1건 반환 등).

---

## 완료

*(완료한 항목은 여기로 옮기거나 체크 후 이 섹션을 비워 두세요.)*
