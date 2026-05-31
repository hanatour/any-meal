# Google Maps Platform 사용법 (조사 정리)

any-meal에서 **지도 표시**(Maps JavaScript API)와 **근처 장소 검색**(Places API 등)을 붙일 때 참고합니다. 항상 [공식 문서](https://developers.google.com/maps/documentation)가 최신 기준입니다.

---

## 1. 준비 사항

| 항목 | 설명 |
|------|------|
| Google 계정 | [Google Cloud Console](https://console.cloud.google.com) 로그인 |
| 프로젝트 | 콘솔 상단에서 프로젝트 생성 또는 선택 |
| 결제 계정 | **실서비스**에서는 Maps Platform 사용을 위해 결제(청구) 계정 연결이 필요합니다. |
| API 키 | **사용자 인증 정보**에서 만들고, 사용할 API만 켠 뒤 키에 제한을 걸기 |

**프로토타입만 할 때**  
Maps JavaScript API에 한해, 결제 정보 없이 쓸 수 있는 **Maps Demo Key** 옵션이 문서에 안내되어 있습니다([Maps JavaScript API – API 키](https://developers.google.com/maps/documentation/javascript/get-api-key)). 프로덕션용이 아닙니다.

---

## 2. 사용할 API 켜기

**API 및 서비스 → 라이브러리**에서 필요한 것만 검색해 **사용**합니다.

| 목적 | 대표적인 API |
|------|----------------|
| 브라우저에서 지도 그리기 | **Maps JavaScript API** |
| 서버에서 주변 식당 등 검색 | **Places API** (웹 서비스의 **Nearby Search (New)** 등) |
| 주소·좌표 변환 등 | **Geocoding API** 등 (필요 시) |

지도만 보여주고 검색은 카카오만 쓸 경우, Maps JavaScript API만 켜도 됩니다.

---

## 3. API 키 만들기와 제한

1. **API 및 서비스 → 사용자 인증 정보 → + 사용자 인증 정보 만들기 → API 키**
2. 생성된 키를 복사해 환경 변수 등에 저장 (예: `ANYMEAL_GOOGLE_API_KEY`)
3. 키 설정에서 **애플리케이션 제한**·**API 제한**을 설정하는 것을 권장합니다.
   - 프론트용 키: HTTP 리퍼러(웹사이트) 제한
   - 백엔드 전용 키: 서버 IP 제한 + Places 등 필요한 API만 허용

---

## 4. Maps JavaScript API (지도 표시)

개요: 페이지에 스크립트를 로드할 때 `key=` 파라미터에 API 키를 넣고, JS로 `google.maps.Map` 등을 생성합니다.

**스크립트 로드 예시** (Bootstrap Loader 권장 방식은 [공식 로딩 가이드](https://developers.google.com/maps/documentation/javascript/load-maps-js-api) 참고):

```html
<script
  src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initMap"
  async
  defer
></script>
```

```javascript
function initMap() {
  const center = { lat: 37.572, lng: 126.984 };
  const map = new google.maps.Map(document.getElementById("map"), {
    zoom: 15,
    center: center,
  });
}
```

- 키는 클라이언트에 노출되므로, **리퍼러 제한**과 **API 제한**이 중요합니다.
- 상세: [Maps JavaScript API 문서](https://developers.google.com/maps/documentation/javascript/overview)

---

## 5. Places API – Nearby Search (New) (서버에서 주변 검색)

**근처 장소**를 서버(Java 등)에서 검색할 때 쓰는 **웹 서비스** 예시입니다.

- **엔드포인트**: `POST https://places.googleapis.com/v1/places:searchNearby`
- **메서드**: **POST만** 지원 (예전 GET 방식과 다름)
- **인증 헤더**: `X-Goog-Api-Key: <API_KEY>`
- **필수**: **Field Mask** — 응답에 포함할 필드를 지정해야 합니다.  
  헤더 예: `X-Goog-FieldMask: places.displayName,places.formattedAddress`  
  (필드마다 과금 SKU가 다를 수 있으므로 필요한 필드만 요청하는 것이 좋습니다. `*`는 개발 편의용이며 프로덕션에서는 비권장.)

**요청 본문 예시** (원 반경 내 `restaurant`):

```json
{
  "includedTypes": ["restaurant"],
  "maxResultCount": 10,
  "locationRestriction": {
    "circle": {
      "center": { "latitude": 37.572, "longitude": 126.984 },
      "radius": 500.0
    }
  }
}
```

**cURL 예시** ([공식 문서](https://developers.google.com/maps/documentation/places/web-service/nearby-search)와 동일한 패턴):

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -H "X-Goog-Api-Key: YOUR_API_KEY" \
  -H "X-Goog-FieldMask: places.displayName,places.formattedAddress" \
  -d '{ ... }' \
  "https://places.googleapis.com/v1/places:searchNearby"
```

- 상세: [Nearby Search (New)](https://developers.google.com/maps/documentation/places/web-service/nearby-search)
- Place 리소스 필드: [Place (REST)](https://developers.google.com/maps/documentation/places/web-service/reference/rest/v1/places)

---

## 6. 과금·SKU

Places·Maps 등 **API·필드별 SKU**가 나뉘어 과금됩니다.  
요청 전 [가격 및 과금](https://developers.google.com/maps/billing-and-pricing)과 각 API의 SKU 설명을 확인하세요.

---

## 7. any-meal에 붙일 때 참고

- **지도만**: 프론트에 Maps JS + 리퍼러 제한된 키.
- **추천 식당 검색만 서버에서**: Spring `RestTemplate` / `WebClient`로 `searchNearby` POST 호출, 응답을 기존 `Restaurant` DTO에 맞게 매핑하는 작업이 필요합니다(필드 구조가 Kakao와 다름).
- **키 분리**: 브라우저용 키와 서버용 키를 나누는 것이 안전합니다.

---

## 참고 링크

- [Maps JavaScript API – API 키 받기](https://developers.google.com/maps/documentation/javascript/get-api-key)
- [Maps JavaScript API – 개요](https://developers.google.com/maps/documentation/javascript/overview)
- [Places API – Nearby Search (New)](https://developers.google.com/maps/documentation/places/web-service/nearby-search)
- [API 키 모범 사례](https://developers.google.com/maps/api-security-best-practices)
