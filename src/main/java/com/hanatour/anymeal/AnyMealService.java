package com.hanatour.anymeal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public final class AnyMealService {

    private final Map<String, RestaurantSearchSource> restaurantSearchSourceMap;

    public AnyMealService(Map<String, RestaurantSearchSource> restaurantSearchSourceMap) {
        this.restaurantSearchSourceMap = restaurantSearchSourceMap;
    }

    /**
     * 랜덤으로 근처의 식당을 하나 추천한다.
     * source 파라미터로 단일 소스 사용 또는 콤마 구분 폴백 순서를 지정할 수 있다.
     *
     * @param x      경도 (생략 시 기본값)
     * @param y      위도 (생략 시 기본값)
     * @param source 소스 이름 (예: kakao) 또는 폴백 순서 (예: kakao,naver,google). 기본값 kakao
     * @return 식당 정보
     */
    public Optional<Restaurant> getRestaurantNear(String x, String y, String source) {
        if (source == null || source.isBlank()) {
            source = "kakao";
        }
        final List<String> sourceOrder = Arrays.stream(source.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        if (sourceOrder.isEmpty()) {
            sourceOrder.add("kakao");
        }

        for (String name : sourceOrder) {
            RestaurantSearchSource provider = restaurantSearchSourceMap.get(name);
            if (provider == null) {
                log.debug("Restaurant search source not available: {}", name);
                continue;
            }
            Optional<Restaurant> result = provider.searchNear(x, y);
            if (result.isPresent()) {
                log.debug("getRestaurantNear found from source: {}", name);
                return result;
            }
        }
        return Optional.empty();
    }
}
