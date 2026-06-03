// 홈 페이지 언어별 라우팅을 검증하는 테스트
package com.hanatour.anymeal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomePageController.class)
class HomePageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("한국어 언어 환경이면 한국어 페이지로 이동")
    void home_redirectsToKoreanPage_whenPreferredLanguageIsKorean() throws Exception {
        mockMvc.perform(get("/").header("Accept-Language", "ko-KR,ko;q=0.9,en;q=0.8"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/ko.html"));
    }

    @Test
    @DisplayName("영어 언어 환경이면 영어 홈으로 표시")
    void home_forwardsToEnglishPage_whenPreferredLanguageIsEnglish() throws Exception {
        mockMvc.perform(get("/").header("Accept-Language", "en-US,en;q=0.9,ko;q=0.8"))
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/en.html"));
    }

    @Test
    @DisplayName("언어 헤더가 없으면 영어 홈으로 표시")
    void home_forwardsToEnglishPage_whenAcceptLanguageIsMissing() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(forwardedUrl("/en.html"));
    }

    @Test
    @DisplayName("기존 인덱스 경로는 영어 페이지로 정리")
    void indexAlias_redirectsToEnglishPage() throws Exception {
        mockMvc.perform(get("/index.html"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/en.html"));
    }
}
