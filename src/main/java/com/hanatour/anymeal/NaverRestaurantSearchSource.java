package com.hanatour.anymeal;

import com.hanatour.anymeal.naver.NaverLocalItem;
import com.hanatour.anymeal.naver.NaverLocalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 네이버 지역 검색 API를 사용하는 식당 검색 소스.
 * <p>설정: anymeal.naver-client-id, anymeal.naver-client-secret
 * 미설정 시 검색 결과를 반환하지 않는다.
 */
@Slf4j
@Component
public final class NaverRestaurantSearchSource implements RestaurantSearchSource {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String LOCAL_API_URL = "https://openapi.naver.com/v1/search/local.json";

    private final String clientId;
    private final String clientSecret;

    public NaverRestaurantSearchSource(
            @Value("${anymeal.naver-client-id:}") String clientId,
            @Value("${anymeal.naver-client-secret:}") String clientSecret) {
        this.clientId = clientId != null ? clientId.trim() : "";
        this.clientSecret = clientSecret != null ? clientSecret.trim() : "";
    }

    @Override
    public Optional<Restaurant> searchNear(String x, String y) {
        if (clientId.isEmpty() || clientSecret.isEmpty()) {
            log.debug("Naver API credentials not set, skipping");
            return Optional.empty();
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            var url = UriComponentsBuilder.fromUriString(LOCAL_API_URL)
                    .queryParam("query", "음식점")
                    .queryParam("display", 5)
                    .queryParam("start", 1)
                    .queryParam("sort", "random")
                    .build()
                    .toUriString();

            var response = REST_TEMPLATE.exchange(url, HttpMethod.GET, entity, NaverLocalResponse.class);
            NaverLocalResponse body = response.getBody();
            if (body == null || body.items() == null || body.items().isEmpty()) {
                return Optional.empty();
            }
            List<NaverLocalItem> items = body.items();
            NaverLocalItem item = items.get(ThreadLocalRandom.current().nextInt(items.size()));
            return Optional.of(toRestaurant(item));
        } catch (Exception e) {
            log.warn("Naver local search failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static Restaurant toRestaurant(NaverLocalItem item) {
        String placeName = stripHtml(item.title());
        String link = item.link() != null ? item.link() : "";
        String category = item.category() != null ? item.category() : "";
        String address = item.address() != null ? item.address() : "";
        String roadAddress = item.roadAddress() != null ? item.roadAddress() : "";
        String phone = item.telephone() != null ? item.telephone() : "";
        Long mapx = item.mapx();
        Long mapy = item.mapy();
        // 네이버 mapx/mapy: WGS84 기반 정수. 경도 = 127 + mapx/1e6, 위도 = 37 + mapy/1e6 (한국 영역 보정)
        String xStr = mapx != null ? String.valueOf(127 + mapx / 1_000_000.0) : "";
        String yStr = mapy != null ? String.valueOf(37 + mapy / 1_000_000.0) : "";
        String id = link.isEmpty() ? "naver-" + placeName.hashCode() : link;
        return new Restaurant(
                address,
                null,
                null,
                category,
                null,
                id,
                phone,
                placeName,
                link,
                roadAddress,
                xStr,
                yStr,
                "naver"
        );
    }

    private static String stripHtml(String s) {
        if (s == null) return "";
        return s.replaceAll("<[^>]+>", "").trim();
    }
}
