# any-meal

근처 식당 한 곳을 랜덤으로 추천하는 웹 앱입니다. Kakao 로컬 API·네이버 지역검색 API를 사용하며, 구글 API 연동을 지원할 예정입니다.

## 실행 방법

- **포트**: 8080
- **필수 환경 변수**: 카카오 사용 시 `anymeal.kakao-restapi-key` (아래 "카카오 API 키 생성" 참고). Naver/Google 사용 시에는 해당 섹션의 환경 변수를 추가로 설정합니다.
- 로컬 실행 예:

```bash
./gradlew bootRun
# 또는 API 키 지정:
anymeal.kakao-restapi-key=YOUR_KEY ./gradlew bootRun
```

**로컬 테스트 시 API 키**: 프로젝트 루트에 `.env` 파일을 두면 Gradle이 테스트 실행 시 해당 값을 환경 변수로 넘깁니다. `.env`는 커밋하지 마세요.

```bash
cp .env.example .env   # .env에 사용할 API 키 입력 후
./gradlew test
```

## API

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/restaurant/near` | 근처 식당 1곳 추천. 쿼리 `x`, `y`(경도, 위도) 생략 시 기본 좌표 사용. 추천 없으면 **204 No Content**. 응답에 `source` 필드로 검색 소스(kakao 등) 표시. |
| GET | `/api/config` | 프론트용 공개 설정. `naverMapClientId`: 네이버 지도 표시 시 사용할 클라이언트 ID(없으면 빈 문자열). |

**쿼리 파라미터**

| 이름 | 필수 | 기본값 | 설명 |
|------|------|--------|------|
| x | 아니오 | 설정 기본값 | 경도 |
| y | 아니오 | 설정 기본값 | 위도 |
| source | 아니오 | kakao | 검색 소스. 단일 값(예: `kakao`) 또는 폴백 순서(예: `kakao,naver,google`). 콤마로 나열 시 순서대로 시도해 첫 결과를 반환한다. |

---

## 카카오 API 키 생성

로컬/카테고리 검색 등에는 **REST API 키**가 필요합니다.

1. **카카오 개발자사이트**  
   [https://developers.kakao.com](https://developers.kakao.com) 에 로그인합니다.

2. **앱 만들기**  
   **내 애플리케이션** → **애플리케이션 추가하기**에서 앱 이름을 입력해 앱을 생성합니다.

3. **REST API 키 확인**  
   생성한 앱 선택 → **앱 키** 탭에서 **REST API 키**를 확인합니다.  
   환경 변수: `ANYMEAL_KAKAO_RESTAPI_KEY` (또는 `anymeal.kakao-restapi-key`)

4. **로컬 API 사용 설정**  
   **제품 설정** → **카카오맵** / **로컬**에서 해당 API를 사용 설정합니다.

- [카카오 API 시작하기](https://developers.kakao.com/docs/latest/ko/tutorial/start)
- [로컬 API – 카테고리로 장소 검색](https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category)

---

## 네이버 API 키 생성 (지역검색 / 지도)

네이버 지역검색 API·지도 API를 사용하려면 **Client ID**와 **Client Secret**이 필요합니다.

1. **네이버 개발자센터**  
   [https://developers.naver.com](https://developers.naver.com) 에 로그인합니다.

2. **애플리케이션 등록**  
   상단 **Application** → **애플리케이션 등록**을 선택합니다.

3. **정보 입력**  
   - **애플리케이션 이름**: 서비스명 (예: any-meal)  
   - **사용 API**: **검색** (지역검색 사용 시), **지도** (지도 API 사용 시) 선택  
   - **비로그인 오픈 API**: 이용 목적에 맞게 체크

4. **키 확인**  
   등록 후 **Application** → **내 애플리케이션**에서 해당 앱을 선택하면 **Client ID**, **Client Secret**을 확인할 수 있습니다.  
   환경 변수 예: `ANYMEAL_NAVER_CLIENT_ID`, `ANYMEAL_NAVER_CLIENT_SECRET`

- [네이버 검색 API – 지역](https://developers.naver.com/docs/serviceapi/search/local/local.md)
- [네이버 오픈 API 가이드 – 애플리케이션 등록](https://naver.github.io/naver-openapi-guide/appregister.html)

---

## 구글 API 키 생성

Google Places API(장소 검색) 등 사용 시 **API 키**가 필요합니다.

1. **Google Cloud 콘솔**  
   [https://console.cloud.google.com](https://console.cloud.google.com) 에 로그인합니다.

2. **프로젝트 생성 또는 선택**  
   상단 프로젝트 선택에서 **새 프로젝트**로 생성하거나, 기존 프로젝트를 선택합니다.

3. **API 사용 설정**  
   **API 및 서비스** → **라이브러리**에서 사용할 API(예: **Places API**, **Maps JavaScript API**)를 검색해 **사용**을 클릭합니다.

4. **API 키 만들기**  
   **API 및 서비스** → **사용자 인증 정보** → **사용자 인증 정보 만들기** → **API 키**를 선택해 키를 생성합니다.  
   환경 변수 예: `ANYMEAL_GOOGLE_API_KEY` (또는 `anymeal.google-api-key`)

5. **제한 설정 (권장)**  
   생성한 API 키에서 **애플리케이션 제한**, **API 제한**을 설정해 보안을 강화하는 것을 권장합니다.

- [Google Cloud 문서 – API 키 사용](https://cloud.google.com/docs/authentication/api-keys)
- [Places API](https://developers.google.com/maps/documentation/places/web-service)
