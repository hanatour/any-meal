package com.hanatour.anymeal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnyMealController.class)
class AnyMealControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AnyMealConfig anyMealConfig;

    @MockitoBean
    AnyMealService anyMealService;

    @MockitoBean
    LogService logService;

    private static Restaurant sampleRestaurant() {
        return new Restaurant(
            "서울 강남구",
            "FD6",
            "음식점",
            "음식점 > 한식",
            "100",
            "123",
            "02-1234-5678",
            "테스트식당",
            "https://place.map.kakao.com/123",
            "서울 강남구 테스트로 1",
            "127.0",
            "37.5",
            "kakao"
        );
    }

    @Test
    @DisplayName("좌표 전달 시 서비스가 식당을 반환하면 200과 함께 해당 식당 JSON 반환")
    void getRestaurantNear_returns200WithBody_whenRestaurantFound() throws Exception {
        when(anyMealConfig.getDefaultLongitude()).thenReturn("126.98");
        when(anyMealConfig.getDefaultLatitude()).thenReturn("37.57");
        when(anyMealService.getRestaurantNear(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(sampleRestaurant()));

        mockMvc.perform(get("/restaurant/near").param("x", "127.0").param("y", "37.5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.place_name").value("테스트식당"))
            .andExpect(jsonPath("$.place_url").value("https://place.map.kakao.com/123"))
            .andExpect(jsonPath("$.source").value("kakao"));
    }

    @Test
    @DisplayName("추천 결과가 없으면 204 No Content")
    void getRestaurantNear_returns204_whenEmpty() throws Exception {
        when(anyMealConfig.getDefaultLongitude()).thenReturn("126.98");
        when(anyMealConfig.getDefaultLatitude()).thenReturn("37.57");
        when(anyMealService.getRestaurantNear(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(Optional.empty());

        mockMvc.perform(get("/restaurant/near").param("x", "127.0").param("y", "37.5"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("x, y 생략 시 기본 좌표로 서비스 호출")
    void getRestaurantNear_usesDefaultCoordinates_whenParamsMissing() throws Exception {
        when(anyMealConfig.getDefaultLongitude()).thenReturn("126.98");
        when(anyMealConfig.getDefaultLatitude()).thenReturn("37.57");
        when(anyMealService.getRestaurantNear("126.98", "37.57", "kakao", "ko"))
            .thenReturn(Optional.of(sampleRestaurant()));

        mockMvc.perform(get("/restaurant/near"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.place_name").value("테스트식당"));

        verify(anyMealService).getRestaurantNear("126.98", "37.57", "kakao", "ko");
    }

    @Test
    @DisplayName("영어 언어 환경이면 기본 소스로 Google 사용")
    void getRestaurantNear_usesGoogleDefault_whenPreferredLanguageIsNotKorean() throws Exception {
        when(anyMealConfig.getDefaultLongitude()).thenReturn("126.98");
        when(anyMealConfig.getDefaultLatitude()).thenReturn("37.57");
        when(anyMealService.getRestaurantNear(eq("127.0"), eq("37.5"), eq("google"), eq("en-US")))
            .thenReturn(Optional.of(sampleRestaurant()));

        mockMvc.perform(get("/restaurant/near")
                .param("x", "127.0")
                .param("y", "37.5")
                .header("Accept-Language", "en-US,en;q=0.9,ko;q=0.8"))
            .andExpect(status().isOk());

        verify(anyMealService).getRestaurantNear("127.0", "37.5", "google", "en-US");
    }

    @Test
    @DisplayName("source가 명시되면 언어 환경보다 우선한다")
    void getRestaurantNear_usesExplicitSource_whenSourceProvided() throws Exception {
        when(anyMealConfig.getDefaultLongitude()).thenReturn("126.98");
        when(anyMealConfig.getDefaultLatitude()).thenReturn("37.57");
        when(anyMealService.getRestaurantNear(eq("127.0"), eq("37.5"), eq("kakao"), eq("en-US")))
            .thenReturn(Optional.of(sampleRestaurant()));

        mockMvc.perform(get("/restaurant/near")
                .param("x", "127.0")
                .param("y", "37.5")
                .param("source", "kakao")
                .header("Accept-Language", "en-US,en;q=0.9"))
            .andExpect(status().isOk());

        verify(anyMealService).getRestaurantNear("127.0", "37.5", "kakao", "en-US");
    }

    @Test
    @DisplayName("대한민국 밖 좌표이면 한국어 환경이어도 기본 소스로 Google 사용")
    void getRestaurantNear_usesGoogleDefault_whenCoordinateIsOutsideSouthKorea() throws Exception {
        when(anyMealConfig.getDefaultLongitude()).thenReturn("126.98");
        when(anyMealConfig.getDefaultLatitude()).thenReturn("37.57");
        when(anyMealService.getRestaurantNear(eq("-73.9857"), eq("40.7484"), eq("google"), eq("ko-KR")))
            .thenReturn(Optional.of(sampleRestaurant()));

        mockMvc.perform(get("/restaurant/near")
                .param("x", "-73.9857")
                .param("y", "40.7484")
                .header("Accept-Language", "ko-KR,ko;q=0.9"))
            .andExpect(status().isOk());

        verify(anyMealService).getRestaurantNear("-73.9857", "40.7484", "google", "ko-KR");
    }
}
