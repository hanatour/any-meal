# 개선 목록

AI/수동 작업 시 이 문서를 기준으로 진행하면 됩니다. 완료한 항목은 `[x]`로 표시하거나 "완료" 섹션으로 옮기세요.

Last updated: 2026-03

---

## 할 일

- [ ] **개인정보 문구와 로깅 정책 정합성 확보**: FAQ는 위치 정보와 개인정보를 저장하지 않는다고 설명하지만, 서버는 추천 성공 시 좌표와 식당 정보를 `logs/location.log`에 남기고 Tomcat access log도 활성화되어 있습니다. 좌표 로그 제거 또는 반올림, 보관 기간 명시, FAQ/문서 문구 정정 중 하나를 결정합니다.
- [ ] **네이버 검색 소스의 위치 기반 동작 재검토**: `NaverRestaurantSearchSource.searchNear(x, y)`가 현재 `x`, `y`를 요청에 사용하지 않아 `/restaurant/near?source=naver`가 근처 식당 보장을 하지 못합니다. 네이버 API로 가능한 범위를 다시 정하고, 불가능하면 근처 검색 소스에서 제외하거나 별도 검색 모드로 분리합니다.
- [ ] **네이버 좌표 매핑 테스트 추가**: `mapx`, `mapy`를 앱의 `x`, `y`로 변환하는 로직에 단위 검증 테스트가 없습니다. 샘플 응답 기준으로 유효한 경도·위도 범위가 나오는지 테스트를 추가합니다.
- [ ] **카카오 소스 실패 처리 보강**: 카카오 API 키가 비어 있거나 좌표가 잘못된 경우 예외가 컨트롤러까지 전파될 수 있습니다. Google/Naver 소스처럼 키 미설정 시 건너뛰고, 잘못된 좌표는 400 또는 빈 추천으로 일관되게 처리합니다.
- [ ] **정적 페이지의 응답값 렌더링 안전화**: `index.html`, `en.html`이 외부 장소 API 응답의 `place_name`, `place_url`을 `innerHTML` 문자열로 직접 조립합니다. DOM API와 `textContent`를 사용하고 링크 프로토콜을 검증합니다.
- [ ] **한국어·영어 페이지 공통 스크립트 분리**: 위치 조회, 추천 API 호출, 지도 표시 로직이 두 HTML 파일에 중복되어 있습니다. 공통 JS와 언어별 문구 데이터로 분리해 수정 누락 위험을 줄입니다.
- [ ] **`/log` 엔드포인트 정리**: 현재 `GET /log`는 200과 빈 본문을 반환합니다. 운영 목적이 없으면 제거하고, 필요하면 명확한 응답과 테스트를 추가합니다.
- [ ] **테스트 공백 보완**: Naver 소스, Kakao 키 미설정/잘못된 좌표, `/log`, 정적 HTML 렌더링 안전성에 대한 테스트가 없습니다. 우선순위 높은 버그 재현 테스트부터 추가합니다.
- [ ] **문서 최신화**: README와 기존 개선 목록에 Google API가 "지원 예정"으로 남아 있는 부분을 현재 구현 상태와 맞춥니다.
- [x] **네이버 지역검색 API 연동**: 네이버 지역검색 API를 이용해 업체(식당) 검색 기능 추가. 설정(`anymeal.naver-client-id`, `anymeal.naver-client-secret`) 및 서비스/컨트롤러 설계.
- [x] **네이버 지도 API 연동**: 네이버 지도 API를 이용한 지도 표시 또는 위치 활용 (검색 결과 연동). 프론트 또는 백엔드에서 필요한 범위 정의 후 연동.
- [ ] **구글 API 연동**: Google Places API(또는 검색 API)를 이용한 검색 지원 추가. 설정(`anymeal.google-api-key` 등) 및 서비스 레이어 구현.
- [x] **다중 검색 소스 통합**: Kakao / Naver / Google 중 하나 또는 여러 소스를 조합하는 방식 설계 (예: 쿼리 파라미터 `source=kakao|naver|google`, 또는 우선순위/폴백 정책). 단일 `GET /restaurant/near` 응답 형식 유지 여부 결정.
- [x] **문서 및 설정 보강**: README·환경 변수에 Naver/Google API 키 안내 추가. `.env.example`에 새 키 placeholder 추가.

---

## 완료

- **네이버 지역검색 API 연동**: NaverRestaurantSearchSource 추가, `source=naver` 및 폴백 지원. 설정 anymeal.naver-client-id / naver-client-secret.
- **네이버 지도 API 연동**: 프론트에서 추천 결과 좌표가 있으면 `/api/config`로 Naver Map 클라이언트 ID를 받아 지도 표시 및 마커 표시.
- **다중 검색 소스 통합**: `source` 쿼리 파라미터(kakao / 콤마 구분 폴백), `RestaurantSearchSource` 추상화, Kakao 구현체 분리. 단일 `GET /restaurant/near` 및 응답 형식 유지.
- **백엔드 – AnyMealService 생성자**: 생성자 주입으로 정리.
- **백엔드 – 테스트 웹훅 로깅**: `System.out.println` 제거 후 `log` 사용.
- **API – 빈 추천 응답 처리**: 추천 없을 때 204 No Content, README 명시.
- **프론트 – document.write 제거**: `#result` div + innerHTML로 렌더링.
- **프론트 – 빈/부분 응답 처리**: "추천할 식당이 없어요" 등 안내 문구.
- **프론트 – 위치 실패 안내**: 기본 좌표 사용 시 안내 문구 노출.
- **문서 – README 보강**: 실행 방법, API 설명 추가.
- **테스트 – 서비스/API 검증**: AnyMealControllerTest 추가 (모킹).
