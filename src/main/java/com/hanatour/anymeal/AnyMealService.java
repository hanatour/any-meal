package com.hanatour.anymeal;

import com.hanatour.anymeal.geocalc.Coordinate;
import com.hanatour.anymeal.geocalc.EarthCalc;
import com.hanatour.anymeal.geocalc.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public final class AnyMealService {

    private static SecureRandom secureRandom;

    static {
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("SecureRandom.getInstanceStrong()", e);
            secureRandom = new SecureRandom();
        }
    }

    @Value("${anymeal.kakao-restapi-key}")
    private String kakaoRestapiKey;

    private AnyMealService() {
    }

    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * 랜덤으로 근처의 식당을 하나 추천해준다. 카카오 API를 이용하여 구현.
     *
     * @return 식당 정보
     */
    public Optional<Restaurant> getRestaurantNear(String x, String y) {
        final Coordinate lat = Coordinate.fromDegrees(Double.parseDouble(y));
        final Coordinate lng = Coordinate.fromDegrees(Double.parseDouble(x));
        final Point orgPoint = Point.at(lat, lng);
        final Point point1 = EarthCalc.gcd.pointAt(orgPoint, 0., 190.);
        final Point point2 = EarthCalc.gcd.pointAt(orgPoint, 90., 190.);
        final Point point3 = EarthCalc.gcd.pointAt(orgPoint, 180., 190.);
        final Point point4 = EarthCalc.gcd.pointAt(orgPoint, 270., 190.);
        final List<Point> points = Arrays.asList(point1, point2, point3, point4);
        final List<Restaurant> restaurants =
            points.stream()
                .parallel()
                .flatMap(p -> getRestaurantNearAllPages(p, "distance", 500, "FD6", 1).stream())
                .filter(r ->
                    !r.getCategoryName().startsWith("음식점 > 술집") &&
                    !r.getCategoryName().startsWith("음식점 > 간식"))
                .distinct()
                .peek(r -> log.debug("{} - {}", r.getCategoryName(), r.getPlaceName()))
                .toList();
        log.debug("getRestaurantNear restaurants: {}", restaurants.size());
        if (restaurants.size() > 1) {
            final int index = Math.abs(secureRandom.nextInt() % restaurants.size());
            return Optional.of(restaurants.get(index));
        }
        return restaurants.stream().findAny();
    }

    public List<Restaurant> getRestaurantNearAllPages(Point point, String sort, int radius,
        String categoryCode, int page) {
        final CategoryResponse categoryResponse = getRestaurantNear(point, sort, radius,
            categoryCode, page);
        if (categoryResponse == null) {
            return Collections.emptyList();
        }
        final List<Restaurant> restaurants = categoryResponse.getRestaurants();
        if (Boolean.TRUE.equals(categoryResponse.getMeta().getEnd())) {
            return restaurants;
        }
        restaurants.addAll(getRestaurantNearAllPages(point, sort, radius, categoryCode, page + 1));
        return restaurants;
    }

    public CategoryResponse getRestaurantNear(Point point, String sort, int radius,
        String categoryCode, int page) {
        log.debug("getRestaurantNear point: {}, sort: {}, radius: {}, categoryCode: {}, page: {}",
            point, sort, radius, categoryCode, page);
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestapiKey);
        final HttpEntity<Void> entity = new HttpEntity<>(headers);

        final String url = "https://dapi.kakao.com/v2/local/search/category.json";
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("x", point.longitude)
            .queryParam("y", point.latitude)
            .queryParam("sort", sort)
            .queryParam("radius", radius)
            .queryParam("category_group_code", categoryCode)
            .queryParam("page", page);

        final HttpEntity<CategoryResponse> response = restTemplate.exchange(builder.toUriString(),
            HttpMethod.GET, entity, CategoryResponse.class);

        return response.getBody();
    }

}
