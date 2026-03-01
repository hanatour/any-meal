# any-meal

근처 식당 한 곳을 랜덤으로 추천하는 웹 앱입니다. Kakao 로컬 API(카테고리 검색)를 사용합니다.

## 실행 방법

- **포트**: 8080
- **필수 환경 변수**: `anymeal.kakao-restapi-key` (Kakao REST API 키)
- 로컬 실행 예:

```bash
./gradlew bootRun
# 또는 API 키 지정:
anymeal.kakao-restapi-key=YOUR_KEY ./gradlew bootRun
```

**로컬 테스트 시 API 키**: 프로젝트 루트에 `.env` 파일을 두면 Gradle이 테스트 실행 시 해당 값을 환경 변수로 넘깁니다. `.env`는 커밋하지 마세요.

```bash
cp .env.example .env   # .env에 Kakao REST API 키 입력 후
./gradlew test
```

## API

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/restaurant/near` | 근처 식당 1곳 추천. 쿼리 `x`, `y`(경도, 위도) 생략 시 기본 좌표 사용. 추천 없으면 **204 No Content** |

- [Kakao API Docs](https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category)
