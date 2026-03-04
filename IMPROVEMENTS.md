# 개선 목록

AI/수동 작업 시 이 문서를 기준으로 진행하면 됩니다. 완료한 항목은 `[x]`로 표시하거나 "완료" 섹션으로 옮기세요.

Last updated: 2026-03

---

## 할 일

- [ ] **네이버 지역검색 API 연동**: 네이버 지역검색 API를 이용해 업체(식당) 검색 기능 추가. 설정(`anymeal.naver-client-id`, `anymeal.naver-client-secret`) 및 서비스/컨트롤러 설계.
- [ ] **네이버 지도 API 연동**: 네이버 지도 API를 이용한 지도 표시 또는 위치 활용 (검색 결과 연동). 프론트 또는 백엔드에서 필요한 범위 정의 후 연동.
- [ ] **구글 API 연동**: Google Places API(또는 검색 API)를 이용한 검색 지원 추가. 설정(`anymeal.google-api-key` 등) 및 서비스 레이어 구현.
- [ ] **다중 검색 소스 통합**: Kakao / Naver / Google 중 하나 또는 여러 소스를 조합하는 방식 설계 (예: 쿼리 파라미터 `source=kakao|naver|google`, 또는 우선순위/폴백 정책). 단일 `GET /restaurant/near` 응답 형식 유지 여부 결정.
- [x] **문서 및 설정 보강**: README·환경 변수에 Naver/Google API 키 안내 추가. `.env.example`에 새 키 placeholder 추가.

---

## 완료

- **백엔드 – AnyMealService 생성자**: 생성자 주입으로 정리.
- **백엔드 – 테스트 웹훅 로깅**: `System.out.println` 제거 후 `log` 사용.
- **API – 빈 추천 응답 처리**: 추천 없을 때 204 No Content, README 명시.
- **프론트 – document.write 제거**: `#result` div + innerHTML로 렌더링.
- **프론트 – 빈/부분 응답 처리**: "추천할 식당이 없어요" 등 안내 문구.
- **프론트 – 위치 실패 안내**: 기본 좌표 사용 시 안내 문구 노출.
- **문서 – README 보강**: 실행 방법, API 설명 추가.
- **테스트 – 서비스/API 검증**: AnyMealControllerTest 추가 (모킹).
