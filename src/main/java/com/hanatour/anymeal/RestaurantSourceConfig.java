package com.hanatour.anymeal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RestaurantSourceConfig {

    @Bean
    public Map<String, RestaurantSearchSource> restaurantSearchSourceMap(
            KakaoRestaurantSearchSource kakaoRestaurantSearchSource,
            GoogleRestaurantSearchSource googleRestaurantSearchSource,
            NaverRestaurantSearchSource naverRestaurantSearchSource) {
        return Map.of(
                "kakao", kakaoRestaurantSearchSource,
                "google", googleRestaurantSearchSource,
                "naver", naverRestaurantSearchSource);
    }
}
