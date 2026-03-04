package com.hanatour.anymeal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnyMealServiceTest {

    @Test
    @DisplayName("source가 kakao이면 해당 소스만 호출하고 결과를 반환한다")
    void getRestaurantNear_returnsFromSingleSource() {
        RestaurantSearchSource kakao = mock(RestaurantSearchSource.class);
        Restaurant expected = new Restaurant(
            "addr", "FD6", "음식점", "한식", "100", "1", null, "테스트", "http://kakao.com/1",
            null, "127.0", "37.5", "kakao");
        when(kakao.searchNear(anyString(), anyString())).thenReturn(Optional.of(expected));

        AnyMealService service = new AnyMealService(Map.of("kakao", kakao));
        Optional<Restaurant> result = service.getRestaurantNear("127.0", "37.5", "kakao");

        assertTrue(result.isPresent());
        assertEquals("테스트", result.get().placeName());
        assertEquals("kakao", result.get().source());
    }

    @Test
    @DisplayName("source에 콤마로 여러 소스가 있으면 순서대로 시도하고 첫 결과를 반환한다")
    void getRestaurantNear_fallbackToNextSource() {
        RestaurantSearchSource kakao = mock(RestaurantSearchSource.class);
        RestaurantSearchSource naver = mock(RestaurantSearchSource.class);
        when(kakao.searchNear(anyString(), anyString())).thenReturn(Optional.empty());
        Restaurant fromNaver = new Restaurant(
            "addr", null, null, null, null, "2", null, "네이버식당", null, null, "127.0", "37.5", "naver");
        when(naver.searchNear(anyString(), anyString())).thenReturn(Optional.of(fromNaver));

        AnyMealService service = new AnyMealService(Map.of("kakao", kakao, "naver", naver));
        Optional<Restaurant> result = service.getRestaurantNear("127.0", "37.5", "kakao,naver");

        assertTrue(result.isPresent());
        assertEquals("네이버식당", result.get().placeName());
    }

    @Test
    @DisplayName("모든 소스가 비어 있으면 empty 반환")
    void getRestaurantNear_returnsEmpty_whenAllEmpty() {
        RestaurantSearchSource kakao = mock(RestaurantSearchSource.class);
        when(kakao.searchNear(anyString(), anyString())).thenReturn(Optional.empty());

        AnyMealService service = new AnyMealService(Map.of("kakao", kakao));
        Optional<Restaurant> result = service.getRestaurantNear("127.0", "37.5", "kakao");

        assertTrue(result.isEmpty());
    }
}
